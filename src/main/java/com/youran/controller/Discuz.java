package com.youran.controller;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 阿里云视觉平台验证码识别：
 * https://vision.aliyun.com/experience/detail?spm=a211p3.14020179.J_7524944390.27.11ac4b58sPNh55&tagName=ocr&children=RecognizeVerificationcode
 *
 */
public class Discuz {
    private String url = "http://dz.bcaqfy.xin/";
    private String formHash = null;
    //实例化一个浏览器
    CloseableHttpClient httpClient = HttpClients.createDefault();

    public String getFormHash() {
        return formHash;
    }
    //登陆
    public boolean login(String uname, String pwd) throws UnsupportedEncodingException {
        boolean flag = false;
        HttpPost httpPost = new HttpPost(url + "member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1");

        // 设置2个post参数，一个是scope、一个是q
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //创建参数
        NameValuePair username = new BasicNameValuePair("username", uname);
        NameValuePair password = new BasicNameValuePair("password", pwd);
        NameValuePair handlekey = new BasicNameValuePair("handlekey", "ls");
        NameValuePair fastloginfield = new BasicNameValuePair("fastloginfield", "username");
        NameValuePair quickforward = new BasicNameValuePair("quickforward", "yes");
        params.add(username);
        params.add(password);
        params.add(handlekey);
        params.add(fastloginfield);
        params.add(quickforward);

        //构建一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
        //将实体添加到httpPost
        httpPost.setEntity(formEntity);
        //返回的响应
        CloseableHttpResponse response = null;
        try {
            //执行请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //返回内容
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                if (!content.contains("登录失败")) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            httpClient.close();
        }
        return flag;

    }

    //获取formhash
    public void getFromHash() throws IOException {
        String formHash = null;
        HttpGet httpGet = new HttpGet(url + "member.php?mod=register");
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Document document = Jsoup.parse(content);
                Element scbar_form = document.getElementById("scbar_form");
                Elements input = scbar_form.getElementsByTag("input");
                for (Element el : input) {
                    if ("formhash".equals(el.attr("name"))) {
                        this.formHash = el.attr("value");
                    }
                }
            }
        } finally {
            if (response != null) {
                response.close();
            }
            //相当于关闭浏览器
//            httpClient.close();
        }
    }

    //注册
    public boolean register(String uname, String pwd, String email) throws IOException {
        boolean flag = false;
        HttpPost httpPost = new HttpPost(url + "member.php?mod=register&inajax=1");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36");
        httpPost.setHeader("Referer", "http://dz.bcaqfy.xin/member.php?mod=register");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //创建参数
        NameValuePair regsubmit = new BasicNameValuePair("regsubmit", "yes");
        NameValuePair formhash = new BasicNameValuePair("formhash", formHash);
        NameValuePair referer = new BasicNameValuePair("referer", url);
        NameValuePair activationauth = new BasicNameValuePair("activationauth", "");
        NameValuePair NH3X2q = new BasicNameValuePair("NH3X2q", uname);
        NameValuePair rKfRC6 = new BasicNameValuePair("rKfRC6", pwd);
        NameValuePair sif0H3 = new BasicNameValuePair("sif0H3", pwd);
        NameValuePair tjYuSn = new BasicNameValuePair("tjYuSn", email);
        params.add(regsubmit);
        params.add(formhash);
        params.add(referer);
        params.add(activationauth);
        params.add(NH3X2q);
        params.add(rKfRC6);
        params.add(sif0H3);
        params.add(tjYuSn);

        //构建一个form表单式的实体
        System.out.println(params);
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
        //将实体添加到httpPost
        httpPost.setEntity(formEntity);
        //返回的响应
        CloseableHttpResponse response = null;
        try {
            //执行请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //返回内容
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                if (content.contains("感谢您注册")) {
                    System.out.println("注册成功");
                    flag = true;
                } else if (content.contains("该用户名已被注册")) {
                    System.out.println("该用户名已被注册");
                    flag = false;
                } else {
                    System.out.println("注册失败");
                    flag = false;
                }
//                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpClient.close();
        }
        return flag;
    }

    //获取用户信息
    public void getInfo() throws IOException {
        HttpGet httpGet = new HttpGet(url + "home.php?mod=space&uid=15");
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Document document = Jsoup.parse(content);
                //获取好友数、回帖数、主题数
                System.out.println("\n======获取好友数、回帖数、主题数======");
                Elements tjxx = document.getElementsByClass("cl bbda pbm mbm");
                for (Element el : tjxx) {
                    System.out.println(el.getElementsByTag("a").text());
                }
                //账户统计信息
                System.out.println("\n======账户统计信息======");
                System.out.println(document.getElementById("psts").text());
                //获取注册时间、最后访问等信息
                System.out.println("\n======获取注册时间、最后访问等信息======");
                Element pbbs = document.getElementById("pbbs");
                Elements li = pbbs.getElementsByTag("li");
                for (Element el : li) {
                    System.out.println(el.text());
                }
            }
        } finally {
            if (response != null) {
                response.close();
            }
            //相当于关闭浏览器
            httpClient.close();
        }
    }

    //发帖
    public void postMsg(String title, String msg, String fid) throws IOException {
        HttpPost httpPost = new HttpPost(url + "forum.php?mod=post&action=newthread&fid=" + fid + "&extra=&topicsubmit=yes");
        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;charset=utf-8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //创建参数
        NameValuePair file = new BasicNameValuePair("file", "");
        NameValuePair file1 = new BasicNameValuePair("file", "");
        NameValuePair save = new BasicNameValuePair("save", "");
        NameValuePair message = new BasicNameValuePair("message", msg);
        NameValuePair usesig = new BasicNameValuePair("usesig", "1");
        NameValuePair allownoticeauthor = new BasicNameValuePair("allownoticeauthor", "1");
        NameValuePair wysiwyg = new BasicNameValuePair("wysiwyg", "1");
        NameValuePair posttime = new BasicNameValuePair("posttime", String.valueOf(new Date().getTime()).substring(0, 10));
        NameValuePair formhash = new BasicNameValuePair("formhash", formHash);
        NameValuePair subject = new BasicNameValuePair("subject", title);
        params.add(file);
        params.add(file1);
        params.add(save);
        params.add(message);
        params.add(usesig);
        params.add(allownoticeauthor);
        params.add(wysiwyg);
        params.add(posttime);
        params.add(formhash);
        params.add(subject);
        System.out.println(params);
        //构建一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf-8");
        //将实体添加到httpPost
        httpPost.setEntity(formEntity);
        //返回的响应
        CloseableHttpResponse response = null;
        try {
            //执行请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //返回内容
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpClient.close();
        }
    }

}
