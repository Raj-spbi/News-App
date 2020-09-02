package com.aaratechnologies.lawtrend.managers;

public class WebURLS {
    private static final String ROOT_URL = "https://lawtrend.in/wp-json/";
    public static final String ALL_POSTS=ROOT_URL+"wp/v2/posts";
    public static final String ALL_MENUS_LIST=ROOT_URL+"wp-api-menus/v2/menus/122";
    public static final String Category_Wise_News=ROOT_URL+"wp/v2/posts?categories=";
    public static final String Contact_US_Form=ROOT_URL+"contact-form-7/v1/contact-forms/752/feedback";
    public static final String All_States="https://lawtrend.in/pdf-api.php?apicall=category_list";
    public static final String StateWiseCategory="https://lawtrend.in/pdf-api.php?apicall=category_product&cat_id=";
    public static final String CentralBareActs="https://lawtrend.in/pdf-api.php?apicall=category_product&cat_id=3";
    public static final String Search_DATA=ROOT_URL+"wp/v2/posts?=";
    public static final String POSTS_PER_PAGE="https://lawtrend.in/wp-json/wp/v2/posts?page=";
    public static final String Category_WISE_POST_PER_PAGE="https://lawtrend.in/wp-json/wp/v2/posts?categories=";
    public static final String Internship_FORM="https://lawtrend.in/wp-json/contact-form-7/v1/contact-forms/836/feedback";
    public static final String AllPDF_Single_APi="https://lawtrend.in/pdf-api.php?apicall=pdf_list";
}

