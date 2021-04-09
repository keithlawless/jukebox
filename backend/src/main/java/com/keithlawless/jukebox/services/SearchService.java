package com.keithlawless.jukebox.services;

import com.keithlawless.jukebox.entity.Artwork;
import com.keithlawless.jukebox.entity.Folder;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.enums.IndexState;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

@Service
public class SearchService {

    private static final Logger logger = Logger.getLogger(SearchService.class.getName());

    @Value("${com.keithlawless.jukebox.basedir}")
    private String baseDir;

    @Value("${com.keithlawless.jukebox.search.index.location}")
    private String indexPath;

    @Autowired
    private FileService fileService;

    @Autowired
    private TagService tagService;

    private IndexState indexState = IndexState.NONE;

    @PostConstruct
    public void init() {
        logger.info("Building Search Index...");
        indexState = IndexState.INDEXING;
        index();
        logger.info("Search Index Ready...");
    }

    private void index() {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            IndexWriter indexWriter = new IndexWriter(dir, iwc);

            //Recurse through the whole directory tree of songs
            indexFolder(baseDir, indexWriter);

            indexWriter.close();
            indexState = IndexState.READY;
        }
        catch( IOException ioe ) {
            logger.info("IOException caught: " + ioe.toString());
            indexState = IndexState.ERROR;
        }
    }

    private void indexFolder(String entryPoint, IndexWriter indexWriter) {
        Folder folder = fileService.getFolder(entryPoint);

        for(String f: folder.getFolders()) {
            try {
                String url = URLDecoder.decode(f, StandardCharsets.UTF_8.toString());
                indexFolder(url, indexWriter);
            }
            catch(UnsupportedEncodingException e) {
                logger.info("UnsupportedEncodingException caught: " + e.toString());
            }
        }

        for(String f: folder.getFiles()) {
            try {
                String url = URLDecoder.decode(f, StandardCharsets.UTF_8.name());

                MediaMeta mediaMeta = tagService.readTags(url);

                Document document = new Document();

                Field mrlField = new StringField( "mrl", mediaMeta.getMrl(), Field.Store.YES);
                document.add(mrlField);

                Field artistField = new TextField("artist", mediaMeta.getArtist(), Field.Store.YES);
                document.add(artistField);

                Field albumField = new TextField("album", mediaMeta.getAlbum(), Field.Store.YES);
                document.add(albumField);

                Field titleField = new TextField("title", mediaMeta.getTitle(), Field.Store.YES);
                document.add(titleField);

                indexWriter.addDocument(document);
            }
            catch(UnsupportedEncodingException e ) {
                logger.info("UnsupportedEncodingException caught: " + e.toString());
            }
            catch(IOException ioe) {
                logger.info("IOException when adding document to index: " + ioe.toString());
            }
        }
    }

    public List<MediaMeta> query(String term) {
        Vector<MediaMeta> resultList = new Vector<MediaMeta>();

        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("title", analyzer);

            String queryTerms = "artist:\"" + term + "\" " +
                    "album:\"" + term + "\" " +
                    "title:\"" + term + "\"";

            Query query = parser.parse(queryTerms);
            TopDocs topDocs = searcher.search(query, 101);
            ScoreDoc[] hits = topDocs.scoreDocs;

            for(ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);

                MediaMeta mediaMeta = new MediaMeta();
                mediaMeta.setMrl(doc.get("mrl"));
                mediaMeta.setArtist(doc.get("artist"));
                mediaMeta.setAlbum(doc.get("album"));
                mediaMeta.setTitle(doc.get("title"));

                resultList.add(mediaMeta);
            }

        }
        catch(IOException ioe) {
            logger.info("IOException when querying Lucene: " + ioe.toString());
        }
        catch(ParseException pe) {
            logger.info("ParseException when querying Lucene: " + pe.toString());
        }

        return resultList;
    }
}
