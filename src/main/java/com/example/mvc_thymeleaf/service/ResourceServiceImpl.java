package com.example.mvc_thymeleaf.service;

import com.example.mvc_thymeleaf.entity.Resource;
import com.example.mvc_thymeleaf.repository.ResourceRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {
    private static final int PAGE_SIZE = 30;
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Page<Resource> getPage(Integer pageNumber) {
        PageRequest request = PageRequest.of(pageNumber, PAGE_SIZE);
        return resourceRepository.findAll(request);
    }

    @Override
    public Long countResourcesByDiscipline(Long id_disc) {
        return resourceRepository.countResourcesByDiscipline(id_disc);
    }

    @Override
    public Resource addResource(Resource res) {
        return resourceRepository.saveAndFlush(res);
    }

    @Override
    public List<Resource> findNewResources() {
        return resourceRepository.findNewResources();
    }

    @Override
    public Long countResources() {
        return resourceRepository.countResources();
    }

    @Override
    public List<String> getAllPathes() {
        return resourceRepository.getAllPathes();
    }

    @Override
    public Resource getById(Long id) {
        return resourceRepository.getOne(id);
    }

    @Override
    public List<String> getNewPathes() {
        return resourceRepository.getNewPathes();
    }

    @Override
    public List<Resource> getAll() {
        return resourceRepository.findAll();
    }

    @Override
    public List<String> extractDocumentText(String path) {

        try {
            String file_extension = path.lastIndexOf(".") != -1 && path.lastIndexOf(".") != 0
                    ? path.substring(path.lastIndexOf(".") + 1) : "";
            switch (file_extension) {
                case "doc": {
                    FileInputStream INPUT_FILE = new FileInputStream(path);
                    HWPFDocument DOC_FILE = new HWPFDocument(INPUT_FILE);
                    WordExtractor DOC_extractor = new WordExtractor(DOC_FILE);
                    List<String> DOC_text = Arrays.asList(DOC_extractor.getParagraphText());
                    DOC_extractor.close();
                    DOC_FILE.close();
                    INPUT_FILE.close();
                    return DOC_text;
                }
                case "docx": {
                    List<String> DOCX_text = new ArrayList<String>();
                    File file1 = new File(path);
                    try {
                        XWPFDocument DOCX_FILE = new XWPFDocument(OPCPackage.open(file1));
                        List<XWPFParagraph> DOCX_paragraphs = DOCX_FILE.getParagraphs();
                        for (XWPFParagraph p : DOCX_paragraphs) {
                            DOCX_text.add(p.getText());
                        }

                        DOCX_FILE.close();
                        return DOCX_text;

                    } catch (InvalidFormatException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        return DOCX_text;
                    }
                }
                case "pdf": {
                    File file2 = new File(path);
                    PDDocument PDF_FILE = PDDocument.load(file2);
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    List<String> PDF_TEXT = Arrays.asList(pdfStripper.getText(PDF_FILE));
                    PDF_FILE.close();
                    return PDF_TEXT;
                }
                default:
                    return new ArrayList<String>();
            }

        } catch (IOException e) {

            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    @Override
    public Map<String, Double> calculateTerms(List<String> TextDocument) {

        Map<String, Double> TF_words = new HashMap<String, Double>();
        List<String> stop_words;
        boolean flag = false;
        Porters_Stemmer test = new Porters_Stemmer();

        try {
            StringBuilder sb = new StringBuilder();
            // stop_words =
            // Files.readAllLines(Paths.get("C:\\Users\\voropaeva\\workspace\\magistry_1.1\\bin\\predlog_modify.txt"),StandardCharsets.UTF_8);
            //stop_words = Files.readAllLines(Paths.get("/var/www/html/stopiki.txt"), StandardCharsets.UTF_8);
            stop_words = Files.readAllLines(Paths.get("/home/masha/Free/work_eclipse/magistry_1.1/stopiki.txt"), StandardCharsets.UTF_8);
            //stop_words = Files.readAllLines(Paths.get("/src/main/resources/stopiki.txt"), StandardCharsets.UTF_8);
            for (String paragraf : TextDocument) {
                int i = 0;
                while (i < paragraf.length()) {
                    while (i < paragraf.length() && !Character.isLetter(paragraf.charAt(i)))
                        ++i;
                    sb.delete(0, sb.length());
                    while (i < paragraf.length() && Character.isLetter(paragraf.charAt(i))) {
                        sb.append(Character.toLowerCase(paragraf.charAt(i)));
                        ++i;
                    }
                    String word = sb.toString();
                    if (word.length() > 2) {
                        for (String stop_word : stop_words) {

                            if (word.equals(stop_word)) {
                                flag = true;
                                break;

                            }
                            if (flag)
                                break;
                        }

                        if (!flag) {
                            String new_word = test.stem(word);
                            Double count = TF_words.get(new_word);
                            TF_words.put(new_word, count == null ? 1 : count + 1);
                        }

                        flag = false;
                    }
                }
                //size_key_words = TF_words.size();

            }
            return TF_words;
        } catch (IOException e) {

            e.printStackTrace();
            return new HashMap<String, Double>();
        }
    }

    @Override
    public Map<String, Double> sortKeyWords(Map<String, Double> key_words) {
        Map<String, Double> sorted_TF_words = key_words.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x1, x2) -> x1, LinkedHashMap::new));

        return sorted_TF_words;
    }

    @Override
    public Map<String, Double> removeInsignificantTerms(Map<String, Double> sort_dictionary, int limit) {

        /*Double limit_size_map = sort_dictionary.size() * lim;
        int limit = limit_size_map.intValue();*/
        int i = 0;
        for (Iterator<Map.Entry<String, Double>> it = sort_dictionary.entrySet().iterator(); it.hasNext(); i++) {
            it.next();
            if (i > limit) {
                it.remove();
            }
        }
        return sort_dictionary;
    }

    @Override
    public void calculateTermsTF(Map<String, Double> sorted_key_words) {
        for (Map.Entry<String, Double> entry : sorted_key_words.entrySet()) {
            Double count = entry.getValue();
            String term = entry.getKey();
            Double TF = count / sorted_key_words.size();
            sorted_key_words.put(term, TF);
        }
    }

    @Override
    @Transactional
    public Integer updateStatus() {
        return resourceRepository.updateStatus();
    }


}
