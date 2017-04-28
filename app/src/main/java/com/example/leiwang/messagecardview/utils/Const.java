package com.example.leiwang.messagecardview.utils;

import com.android.volley.toolbox.StringRequest;
import com.example.leiwang.messagecardview.controller.AppVolleySingleton;

/**
 * Created by lei.wang on 3/8/2017.
 */

public class Const {

    public static int USER_ID = 0;
    public static String CURRENT_CHANNEL = "";

    public static final String TAG = AppVolleySingleton.class.getSimpleName();
    public static final String FILE_PATH = "Asahi.json";
    public static final String ASAHI = "Asahi";
    public static final String ASAHI_JSON = "https://apiwiki.fxcorporate.com/api/examples/Asahi.json";
    //public static final String GET_JSON_VIA_PHP = "http://10.0.2.2/rssfeed/local_rssfeed/mysqli_get_all_message.php";
    //public static final String GET_JSON_VIA_PHP =  "http://localhost/rssfeed/local_rssfeed/mysqli_get_all_message.php";

    //public static final String USER_REGISTER_PHP = "http://10.0.2.2/rssfeed/user_register.php";
    //public static final String USER_LOGIN_PHP = "http://10.0.2.2/rssfeed/user_login.php";
    //public static final String USER_FORGOT_PASSWORD_PHP = "http://10.0.2.2/rssfeed/user_get_password.php";

    public static final String USER_REGISTER_PHP = "http://japannews.tech/php/user_register.php";
    public static final String USER_LOGIN_PHP = "http://japannews.tech/php/user_login.php";
    public static final String USER_FORGOT_PASSWORD_PHP = "http://japannews.tech/php/user_get_password.php";
    public static final String GET_JSON_VIA_PHP = "http://japannews.tech/php/mysqli_get_all_message.php";
    public static final String GET_MESSAGE_BY_CHANNEL = "http://japannews.tech/php/get_message_by_channel.php";

    public static final String CHANNEL_DOMESTIC = "Domestic";
    public static final String CHANNEL_INTERNATIONAL = "International";
    public static final String CHANNEL_BUSINESS = "Business";
    public static final String CHANNEL_ENTERTAINMENT = "Entertainment";
    public static final String CHANNEL_SPORT = "Sport";
    public static final String CHANNEL_SCIENCE = "Science";
    public static final String CHANNEL_LIFE = "Life";
    public static final String CHANNEL_LOCAL = "Local";
    public static final String CHANNEL_MAGAZINE = "Magazine";

    //public static final String ASAHI_JSON =  "http://localhost/android_connect/2017-03-14/AbemaTIMES2017-03-14.json";
    public static final String JSON_ARRAY_TAG = "Json_array_tag";
    public static final String ITEM = "item";
    public static final String MESSAGE_ID = "message_id";
    public static final String ID = "id";
    public static final String PUB_DATE = "pub_date";
    public static final String SOURCE_NAME = "source_name";
    public static final String TITLE = "title";
    public static final String CHANNEL = "channel";
    public static final String TIME = "time";
    public static final String LINK = "link";
    public static final String IMAGE_URL = "image_url";
    public static final String HAS_IMAGE = "has_image";
    public static final String IMAGE_WIDTH = "image_width";
    public static final String IMAGE_HEIGHT = "image_height";
    public static final String CONTENTS_URL = "contents_url";
    public static final String CONTENTS = "contents";
    public static final int WORD_HEIGHT = 40;
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String OOPS = "Oops";
    public static final String INPUT_WRONG = "Please make sure you entered all the fields correctly.";
    public static final String INPUT_WRONG_NOT_THE_SAME = "Email not the same or password not the same";
    public static final String LOG_IN_SUCCESSFULLY = "Login Successfully!";
    public static final String COULD_NOT_LOGIN = "Could not login";
    public static final String COULD_NOT_GET_PASSWORD = "Could not get password";
    public static final String YOUR_PASSWORD_IS = "Your password is: ";


}
