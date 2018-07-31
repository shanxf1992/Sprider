package com.itheima.sprider_QiDian;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        QiDian_Sprider qiDian_sprider = new QiDian_Sprider();

        //起点 url
        String url = "https://www.qidian.com/";
        try {
            //获取小说的地址列表
            List<String> novelLinkFromQiDian = qiDian_sprider.getNovelLinkFromQiDian(url);

            //获取小说详情页地址列表
            List<String> novelDetailPagesLinks = qiDian_sprider.getNovelDetailPagesLinks(novelLinkFromQiDian);

            //获取小说所有章节内容
            qiDian_sprider.getNovelTexts(novelDetailPagesLinks);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
