package com.goertek;

import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

public class RestTest {

    @Test
    public void test01() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "wm_custom_login_username=tyrion.song; wm_custom_login_wm_ssl=0");
        headers.setOrigin("http://mail.goertek.com");

        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.TEXT_HTML);
        list.add(MediaType.APPLICATION_XHTML_XML);
        list.add(MediaType.APPLICATION_XML);
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        headers.setAccept(list);

        headers.setConnection("keep-alive");
        headers.set("Referer", "http://mail.goertek.com/");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers.set("Host", "mail.goertek.com");
        headers.set("Accept-Encoding", "gzip, deflate");
        headers.setCacheControl("max-age=0");
        headers.set("Upgrade-Insecure-Requests", "1");
        headers.set("Accept-Language", "zh-CN,zh;q=0.9");
        headers.setContentLength(229L);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.put("chr", Arrays.asList("gb"));
        map.put("func", Arrays.asList("login"));
        map.put("isp_domain", Arrays.asList(""));
        map.put("verifycookie", Arrays.asList(""));
        map.put("verifyip", Arrays.asList(""));
        map.put("buttonType", Arrays.asList("1"));
        map.put("usr", Arrays.asList("tyrion.song"));
        map.put("domain", Arrays.asList("goertek.com"));
        map.put("domainType", Arrays.asList("wm"));
        map.put("encode", Arrays.asList("on"));
        map.put("username", Arrays.asList("tyrion.song"));
        map.put("pass", Arrays.asList("$1101;$1051;$1151;$1041;$1051;$501;$981;$331;"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        Map<String, String> uriV = new HashMap<String, String>();
        uriV.put("host", "mail.goertek.com");
        uriV.put("_t", "" + new Date().getTime());

        RestTemplate rest1 = new RestTemplate();

        ResponseEntity<String> response1 = rest1.postForEntity("http://mail.goertek.com/xmweb?host=mail.goertek.com&_t=" + new Date().getTime(), request, String.class);

        System.out.println(response1.getStatusCodeValue());
        HttpHeaders responseHeaders1 = response1.getHeaders();

        Set<Map.Entry<String, List<String>>> entrySet = responseHeaders1.entrySet();
        Iterator<Map.Entry<String, List<String>>> i = entrySet.iterator();
        while (i.hasNext()) {
            Map.Entry<String, List<String>> e = i.next();

            String key = e.getKey();
            System.out.println(key + ":");

            List<String> valueList = e.getValue();
            for (String value : valueList) {
                System.out.println("\t" + value);
            }
        }

        List<String> list2 = responseHeaders1.get("Set-Cookie");
        String cookies = "";
        for (String s : list2) {
            cookies += s + ";";
        }
        for (String s : headers.get("Cookie")) {
            cookies += s + ";";
        }
        headers.set("Cookie", cookies);

        URI uri = responseHeaders1.getLocation();
        ResponseEntity<String> response2 = rest1.exchange(uri, HttpMethod.GET, request, String.class);


        System.out.println(response2.getStatusCodeValue());
        HttpHeaders responseHeaders2 = response2.getHeaders();

        Set<Map.Entry<String, List<String>>> entrySet2 = responseHeaders1.entrySet();
        Iterator<Map.Entry<String, List<String>>> i2 = entrySet2.iterator();
        while (i2.hasNext()) {
            Map.Entry<String, List<String>> e = i2.next();

            String key = e.getKey();
            System.out.println(key + ":");

            List<String> valueList = e.getValue();
            for (String value : valueList) {
                System.out.println("\t" + value);

            }
        }


    }

    @Test
    public void test02() {
        System.out.println(new Date().getTime());
    }


}
