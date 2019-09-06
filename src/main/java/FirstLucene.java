import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FirstLucene {


    @Test
    public void creatIndex() throws Exception {

//      1   创建一个Director对象，指定索引库保存的位置
        Directory directory = FSDirectory.open(new File("E:\\index").toPath());
//      2   基于Director对象创建一个IndexWriter对象

        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
//      3   读取磁盘上的文件，对应的每个文件创建一个文档对象
        File dir = new File("D:\\source");
        File[] files = dir.listFiles();
        for (File file : files) {

            String fileName = file.getName();
            String filePath = file.getPath();
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            long fileSize = FileUtils.sizeOf(file);
//                创建域
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
            Field fieldSize = new TextField("size", fileSize + "", Field.Store.YES);
            //      4、 向文档对象中添加域

            Document document = new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            //      5、  把文档对象写入索引域
            indexWriter.addDocument(document);
        }

        indexWriter.close();

//      6、  关闭indexWriter对象


    }


    @Test
    public void read() throws IOException {
        //            创建一个Directory对象

        Directory directory = FSDirectory.open(new File("E:\\index").toPath());


        IndexReader indexReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("content", "apple"));

        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总记录数" + topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc sc : scoreDocs) {
//               取文档id
            int docId = sc.doc;
            Document doc = indexSearcher.doc(docId);
            System.out.println(doc.get("name"));
            System.out.println(doc.get("path"));
            System.out.println(doc.get("content"));

        }


    }
}
