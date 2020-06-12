package com.tsinghua.tsinghelper.util;

public class TaskInfoUtil {
    // all the fields below must be consistent to the backend

    // required fields' name
    public static String ID = "id";
    public static String TYPE = "type";
    public static String TITLE = "title";
    public static String DESC = "description";
    public static String REWARD = "reward";
    public static String IS_PROCEEDING = "is_proceeding";
    public static String IS_DONE = "is_done";
    public static String IS_PAID = "is_paid";
    public static String START_TIME = "start_time";
    public static String END_TIME = "end_time";
    public static String TIMES_PER_PERSON = "times_per_person";
    public static String TIMES_TOTAL = "times_total";
    public static String TIMES_FINISHED = "times_finished";
    public static String REVIEW_TIME = "review_time";
    public static String PUBLISHER_ID = "publisherId";

    // optional fields's name for task of type meal
    public static String SITE = "site";
    public static String FOOD_NUM = "food_num";

    // optional fields' name for task of type study
    public static String SUBJECTS = "subjects";
    public static String DEMANDS = "demands";

    // optional fields' name for task of type questionnaire
    public static String LINK = "link";
}
