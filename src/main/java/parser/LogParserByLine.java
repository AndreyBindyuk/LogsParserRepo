package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Andrey_Bindyuk on 2/6/2017.
 */
public class LogParserByLine {

    List<String> nieai = new ArrayList<>();
    List<String> common = new ArrayList<>();
    StringBuilder listString = new StringBuilder();
    private final String XMLVERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final String NIEAISERVICES = "NIEAIServices";
    private final String NISERVICES = "NIServices";
    private final String UFXMSG = "UFXMsg";

    public List<String> parseLogsFileByLine(File file, String tagName) {
        String starttag = Objects.equals(tagName, "UFXMsg") ? "<" + tagName : "<" + tagName + ">";
        String endNtag = "</" + tagName + ">";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String coline;
            while ((line = br.readLine()) != null) {
                coline = line.startsWith(XMLVERSION) ? line.substring(XMLVERSION.length(), line.length()).trim() : line.trim();
                if (coline.startsWith(starttag) && coline.endsWith(endNtag)) {
                    nieai.add(coline.substring(0, coline.length()));
                } else if (coline.startsWith(starttag) && !coline.endsWith(endNtag)) {
                    common.add(coline);
                    while (((coline = br.readLine()) != null) && !coline.endsWith(endNtag)) {
                        common.add(coline);
                    }
                    for (String s : common) {
                        if (starttag.contains(NIEAISERVICES) || starttag.contains(NISERVICES)) {
                            if (!s.equals("    </body>")) {
                                listString.append(s);
                            } else {
                                nieai.add(String.valueOf(listString + "</body>" + endNtag).replaceAll("\\s", ""));
                            }
                        } else if (starttag.contains(UFXMSG)) {
                            if (!Objects.equals(s, common.get(common.size() - 1))) {
                                listString.append(s);
                            } else {
                                listString.append(common.get(common.size() - 1));
                                nieai.add(String.valueOf(listString + endNtag).replaceAll("\\s", ""));
                            }
                        }
                    }
                    common.clear();
                    listString.setLength(0);
                } else if (!coline.startsWith(starttag) && coline.endsWith(starttag)) {
                    while (((coline = br.readLine()) != null) && !coline.endsWith(" - [?:log:?] ")) {
                        common.add(coline);
                    }
                    for (String s : common) {
                        if (!s.equals("    </body> ")) {
                            listString.append(s);
                        } else {
                            nieai.add(String.valueOf(starttag + listString + "</body>" + endNtag).replaceAll("\\s", ""));
                        }
                    }
                    common.clear();
                    listString.setLength(0);
                } else if (!coline.startsWith(starttag) && coline.contains(XMLVERSION)) {
                    String xmlVerSub = coline.substring(coline.indexOf(XMLVERSION), coline.length());
                    nieai.add(xmlVerSub.substring(XMLVERSION.length(), xmlVerSub.length()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return nieai;
    }
}
