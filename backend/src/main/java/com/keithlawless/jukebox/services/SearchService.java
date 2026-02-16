package com.keithlawless.jukebox.services;

import com.keithlawless.jukebox.entity.Folder;
import com.keithlawless.jukebox.entity.MediaMeta;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

@Service
public class SearchService {

    private static final Logger logger = Logger.getLogger(SearchService.class.getName());

    private static final String TITLE_FIELD = "title";
    private static final String ALBUM_FIELD = "album";
    private static final String ARTIST_FIELD = "artist";
    private static final String MRL_FIELD = "mrl";

    @Value("${com.keithlawless.jukebox.basedir}")
    private String baseDir;

    @Value("${com.keithlawless.jukebox.search.index.location}")
    private String indexPath;

    private final FileService fileService;
    private final TagService tagService;
    
    private volatile boolean indexReady = false;
    private volatile boolean indexing = false;

    public SearchService(FileService fileService, TagService tagService) {
        this.fileService = fileService;
        this.tagService = tagService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void buildIndexAsync() {
        logger.log(Level.INFO, "Building Search Index asynchronously...");
        indexing = true;
        try {
            index();
            indexReady = true;
            logger.log(Level.INFO, "Search Index Ready...");
        } finally {
            indexing = false;
        }
    }
    
    public boolean isIndexReady() {
        return indexReady;
    }
    
    public boolean isIndexing() {
        return indexing;
    }

    private void index() {
        try (Directory dir = FSDirectory.open(Paths.get(indexPath))) {
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            IndexWriter indexWriter = new IndexWriter(dir, iwc);

            //Recurse through the whole directory tree of songs
            indexFolder(baseDir, indexWriter);

            indexWriter.close();
        }
        catch( IOException ioe ) {
            logger.info("IOException caught: " + ioe);
        }
    }

    private void indexFolder(String entryPoint, IndexWriter indexWriter) {
        Folder folder = fileService.getFolder(entryPoint);

        for(String f: folder.getFolders()) {
            String url = URLDecoder.decode(f, StandardCharsets.UTF_8);
            indexFolder(url, indexWriter);
        }

        for(String f: folder.getFiles()) {
            try {
                String url = URLDecoder.decode(f, StandardCharsets.UTF_8);

                MediaMeta mediaMeta = tagService.readTags(url);

                Document document = new Document();

                Field mrlField = new StringField( MRL_FIELD, mediaMeta.getMrl(), Field.Store.YES);
                document.add(mrlField);

                Field artistField = new TextField(ARTIST_FIELD, mediaMeta.getArtist(), Field.Store.YES);
                document.add(artistField);

                Field albumField = new TextField(ALBUM_FIELD, mediaMeta.getAlbum(), Field.Store.YES);
                document.add(albumField);

                Field titleField = new TextField(TITLE_FIELD, mediaMeta.getTitle(), Field.Store.YES);
                document.add(titleField);

                indexWriter.addDocument(document);
            }
            catch(UnsupportedEncodingException e ) {
                logger.info("UnsupportedEncodingException caught: " + e);
            }
            catch(IOException ioe) {
                logger.info("IOException when adding document to index: " + ioe);
            }
        }
    }

    public List<MediaMeta> query(String term) {
        List<MediaMeta> resultList = new ArrayList<>();

        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser(TITLE_FIELD, analyzer);

            String queryTerms = "artist:\"" + term + "\" " +
                    "album:\"" + term + "\" " +
                    "title:\"" + term + "\"";

            Query query = parser.parse(queryTerms);

            TopDocs hits = searcher. search(query, 10);
            StoredFields storedFields = searcher. storedFields();
            for (ScoreDoc hit : hits.scoreDocs) {
                Document doc = storedFields. document(hit. doc);

                MediaMeta mediaMeta = new MediaMeta();
                mediaMeta.setMrl(doc.get(MRL_FIELD));
                mediaMeta.setArtist(doc.get(ARTIST_FIELD));
                mediaMeta.setAlbum(doc.get(ALBUM_FIELD));
                mediaMeta.setTitle(doc.get(TITLE_FIELD));

                resultList.add(mediaMeta);
            }
        }
        catch(IOException ioe) {
            logger.info("IOException when querying Lucene: " + ioe);
        }
        catch(ParseException pe) {
            logger.info("ParseException when querying Lucene: " + pe);
        }

        return resultList;
    }
}
