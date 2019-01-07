package com.goertek;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.DocumentParser;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.dom4j.DocumentHelper.*;

public class Users {

    private static String USERNAME = "tyrion.song";
    private static String PASSWORD = "nishi2b!";
    private static List<String> cookieList = new ArrayList<String>();
    private static String usr = "";
    private static String sid = "";


    public static String pswdEncode(String s) {
        int len = s.length();
        String rs = "";
        for (int i = 0; i < len; i++) {
            char k = s.charAt(i);
            rs += "$" + (int) k + "1" + ";";
        }

        return rs;
    }

    public static void main(String[] args) {
        cookieList.add("wm_custom_login_username=" + USERNAME + ";");
        cookieList.add("wm_custom_login_wm_ssl = 0;");
        System.out.println("=========================STEP 001======================");
        ResponseEntity<String> response1 = step001();
        printResponse(response1);
        System.out.println("=========================STEP 002======================");
        ResponseEntity<String> response2 = step002(response1.getHeaders().getLocation());
        printResponse(response2);
        System.out.println("=========================STEP 003======================");
        ResponseEntity<String> response3 = step003(response2.getHeaders().getLocation());
        printResponse(response3);
        ResponseEntity<JSONArray> response4 = step004();
        printResponse(response4);
        ResponseEntity<JSONArray> response5 = step005();
        printResponse(response5);
        JSONArray jsonArray = response5.getBody();
        Map<String, Dept> deptList = new HashMap<String, Dept>();
        Map<String, Emp> empList = new HashMap<String, Emp>();
        Map<String, Dept> rootDeptList = new HashMap<String, Dept>();
        Map<String, Emp> rootEmpList = new HashMap<String, Emp>();
        int k = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if ("0".equals(jsonObject.getString("dept"))) {
                Dept dept = jsonObject.toJavaObject(Dept.class);
                deptList.put(dept.getId(), dept);
            } else {
                Emp emp = jsonObject.toJavaObject(Emp.class);
                ResponseEntity<String> response6 = step006(emp.getEmail());
                printResponse(response6);
                try {
                    List<String> l= getMatcher("ul.+ul",response6.getBody());
                    Document d = parseText(l.get(0));
                    System.out.println(d);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                empList.put(emp.getEmail(), emp);
            }
            k++;
            System.out.println(k);
        }

        Set<String> deptIdSet = deptList.keySet();
        Iterator<String> deptIdIterator = deptIdSet.iterator();
        while (deptIdIterator.hasNext()) {
            String deptId = deptIdIterator.next();
            Dept dept = deptList.get(deptId);
            if (deptList.containsKey(dept.getPid())) {
                deptList.get(dept.getPid()).addSubDept(dept);
            } else {
                rootDeptList.put(dept.getId(), dept);
            }
            k--;
            System.out.println(k);
        }

        Set<String> empIdSet = empList.keySet();
        Iterator<String> empIdIterator = empIdSet.iterator();
        while (empIdIterator.hasNext()) {
            String empId = empIdIterator.next();
            Emp emp = empList.get(empId);
            if (deptList.containsKey(emp.getPid())) {
                deptList.get(emp.getPid()).addEmp(emp);
            } else {
                rootEmpList.put(emp.getId(), emp);
            }
            k--;
            System.out.println(k);
        }


        Set<String> deptIdSet2 = deptList.keySet();
        Iterator<String> deptIdIterator2 = deptIdSet.iterator();
        while (deptIdIterator2.hasNext()) {
            Dept dept = deptList.get(deptIdIterator2.next());

            if (dept.getChildsNum() != dept.getEmpList().size()) {
                System.out.println(k);

            }

        }


        System.out.println(k);


    }

    public static ResponseEntity<String> step001() {

        // Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //headers.set("Accept-Encoding", "gzip, deflate");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.set("Cache-Control", "max-age=0");
        headers.set("Connection", "keep-alive");
        headers.setContentLength(229L);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie", String.join("", cookieList));
        headers.set("Host", "mail.goertek.com");
        headers.setOrigin("http://mail.goertek.com");
        headers.set("Referer", "http://mail.goertek.com/");
        headers.set("Upgrade-Insecure-Requests", "1");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");

        // Form Data
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.put("chr", Arrays.asList("gb"));
        map.put("func", Arrays.asList("login"));
        map.put("isp_domain", Arrays.asList(""));
        map.put("verifycookie", Arrays.asList(""));
        map.put("verifyip", Arrays.asList(""));
        map.put("buttonType", Arrays.asList("1"));
        map.put("usr", Arrays.asList(USERNAME));
        map.put("domain", Arrays.asList("goertek.com"));
        map.put("domainType", Arrays.asList("wm"));
        map.put("encode", Arrays.asList("on"));
        map.put("username", Arrays.asList(USERNAME));
        map.put("pass", Arrays.asList(pswdEncode(PASSWORD)));

        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.postForEntity(
                "http://mail.goertek.com/xmweb?host=mail.goertek.com&_t=" + new Date().getTime(),
                new HttpEntity<MultiValueMap<String, String>>(map, headers),
                String.class);

        return response;
    }

    private static ResponseEntity<String> step002(URI uri) {
        // Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //headers.set("Accept-Encoding", "gzip, deflate");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.set("Cache-Control", "max-age=0");
        headers.set("Connection", "keep-alive");
        headers.set("Cookie", String.join("", cookieList));
        headers.set("Host", "mail.goertek.com");
        headers.set("Referer", "http://mail.goertek.com/");
        headers.set("Upgrade-Insecure-Requests", "1");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");


        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.postForEntity(uri,
                new HttpEntity<String>("", headers),
                String.class);
        usr = getMatcher("(?<=usr=)[^&]+", response.getHeaders().getLocation().getQuery()).get(0);
        sid = getMatcher("(?<=sid=)[^&]+", response.getHeaders().getLocation().getQuery()).get(0);
        return response;
    }

    private static ResponseEntity<String> step003(URI uri) {
        // Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //headers.set("Accept-Encoding", "gzip, deflate");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.set("Cache-Control", "max-age=0");
        headers.set("Connection", "keep-alive");
        headers.set("Cookie", String.join("", cookieList));
        headers.set("Host", "mail.goertek.com");
        headers.set("Referer", "http://mail.goertek.com/");
        headers.set("Upgrade-Insecure-Requests", "1");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");


        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.postForEntity(uri,
                new HttpEntity<String>("", headers),
                String.class);

        return response;
    }

    private static ResponseEntity<JSONArray> step004() {
        // Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        //headers.set("Accept-Encoding", "gzip, deflate");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.set("Connection", "keep-alive");
        headers.set("Cookie", String.join("", cookieList));
        headers.set("Host", "mail.goertek.com");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers.set("X-Requested-With", "XMLHttpRequest");

        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(new FastJsonHttpMessageConverter());
        ResponseEntity<JSONArray> response = rest.postForEntity("http://mail.goertek.com/wm2e/mail/personAddressBook/personAddressAction_getAddressInitSys.do?usr=" + usr + "&sid=" + sid,
                new HttpEntity<String>("", headers),
                JSONArray.class);

        return response;
    }


    private static ResponseEntity<JSONArray> step005() {
        // Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        //headers.set("Accept-Encoding", "gzip, deflate");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.set("Connection", "keep-alive");
        headers.set("Cookie", String.join("", cookieList));
        headers.set("Host", "mail.goertek.com");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers.set("X-Requested-With", "XMLHttpRequest");

        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(new FastJsonHttpMessageConverter());
        ResponseEntity<JSONArray> response = rest.postForEntity("http://mail.goertek.com/wm2e/mail/personAddressBook/personAddressAction_getEnterAddressListAllNew.do?usr=" + usr + "&sid=" + sid,
                new HttpEntity<String>("", headers),
                JSONArray.class);

        return response;
    }

    private static ResponseEntity<String> step006(String mail) {
        // Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        //headers.set("Accept-Encoding", "gzip, deflate");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.set("Connection", "keep-alive");
        headers.set("Cookie", String.join("", cookieList));
        headers.set("Host", "mail.goertek.com");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers.set("X-Requested-With", "XMLHttpRequest");

        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(new FastJsonHttpMessageConverter());

        ResponseEntity<String> response = rest.postForEntity("http://mail.goertek.com/wm2e/mail/personAddressBook/personAddressAction_getPersonAddressBookInfo.do?"
                        + "usr=" + usr
                        + "&sid=" + sid
                        + "&email=" + mail + "&iNum=1",
                new HttpEntity<String>("", headers),
                String.class);

        return response;
    }

    public static List<String> getMatcher(String regex, String source) {
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result.add(matcher.group(0));
        }
        return result;
    }

    public static void printResponse(ResponseEntity response) {
        System.out.println(response.getStatusCodeValue());
        HttpHeaders responseHeaders = response.getHeaders();
        Set<Map.Entry<String, List<String>>> entrySet = responseHeaders.entrySet();
        Iterator<Map.Entry<String, List<String>>> i = entrySet.iterator();
        while (i.hasNext()) {
            Map.Entry<String, List<String>> e = i.next();

            String key = e.getKey();
            System.out.println(key + ":");

            List<String> valueList = e.getValue();
            for (String value : valueList) {
                if ("Set-Cookie".equals(key)) {
                    cookieList.addAll(getMatcher(".+=.+;", value));
                }
                System.out.println("\t" + value);
            }
        }
        Object responseBody = response.getBody();
        System.out.println(responseBody);


    }


}
