package cn.yue.base.common.widget.emoji;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.utils.code.TypeUtils;


/**
 * Description : emoji表情初始化
 * Created by yue on 2018/11/17
 */
public final class EmojiConstant {

    private EmojiConstant() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static Context context;

    public static void setContext(Context mContext) {
        context = mContext;
    }

    public static List<Emoji> initEmojiList() {
        if (context == null) {
            return new ArrayList<>();
        }
        try {
            InputStream inputStream = context.getAssets().open("emoji.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder json = new StringBuilder();
            String line = br.readLine();
            while(line != null) {
                json.append(line);
                line = br.readLine();
            }
            List<String> list = new Gson().fromJson(json.toString(), TypeUtils.$List(String.class));

            List<Emoji> emojiList = new ArrayList<>();
            for (String codePoint : list) {
                Emoji emoji = new Emoji();
                if (!TextUtils.isEmpty(codePoint)) {
                    emoji.setIcon(Integer.parseInt(codePoint.substring(2, codePoint.length()), 16));
                    emojiList.add(emoji);
                }
            }
            return emojiList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static final SparseIntArray sEmojisMap = new SparseIntArray();
    private static final SparseIntArray sSoftbanksMap = new SparseIntArray();


    static {
        sEmojisMap.put(0x1f642, R.drawable.emoji_1f642);
        sEmojisMap.put(0x1f616, R.drawable.emoji_1f616);
        sEmojisMap.put(0x1f60d, R.drawable.emoji_1f60d);
        sEmojisMap.put(0x1f62e, R.drawable.emoji_1f62e);
        sEmojisMap.put(0x1f60e, R.drawable.emoji_1f60e);
        sEmojisMap.put(0x1f62d, R.drawable.emoji_1f62d);
        sEmojisMap.put(0x1f914, R.drawable.emoji_1f914);
        sEmojisMap.put(0x1f910, R.drawable.emoji_1f910);
        sEmojisMap.put(0x1f62a, R.drawable.emoji_1f62a);
        sEmojisMap.put(0x1f629, R.drawable.emoji_1f629);
        sEmojisMap.put(0x1f628, R.drawable.emoji_1f628);
        sEmojisMap.put(0x1f621, R.drawable.emoji_1f621);
        sEmojisMap.put(0x1f61b, R.drawable.emoji_1f61b);
        sEmojisMap.put(0x1f62c, R.drawable.emoji_1f62c);
        sEmojisMap.put(0x1f627, R.drawable.emoji_1f627);
        sEmojisMap.put(0x1f641, R.drawable.emoji_1f641);
        sEmojisMap.put(0x1f624, R.drawable.emoji_1f624);
        sEmojisMap.put(0x1f61e, R.drawable.emoji_1f61e);
        sEmojisMap.put(0x1f60a, R.drawable.emoji_1f60a);
        sEmojisMap.put(0x1f644, R.drawable.emoji_1f644);
        sEmojisMap.put(0x1f60b, R.drawable.emoji_1f60b);
        sEmojisMap.put(0x1f635, R.drawable.emoji_1f635);
        sEmojisMap.put(0x1f630, R.drawable.emoji_1f630);
        sEmojisMap.put(0x1f613, R.drawable.emoji_1f613);
        sEmojisMap.put(0x1f603, R.drawable.emoji_1f603);
        sEmojisMap.put(0x1f608, R.drawable.emoji_1f608);
        sEmojisMap.put(0x1f913, R.drawable.emoji_1f913);
        sEmojisMap.put(0x1f632, R.drawable.emoji_1f632);
        sEmojisMap.put(0x1f912, R.drawable.emoji_1f912);
        sEmojisMap.put(0x1f635, R.drawable.emoji_1f635);
        sEmojisMap.put(0x1f622, R.drawable.emoji_1f622);
        sEmojisMap.put(0x1f61c, R.drawable.emoji_1f61c);
        sEmojisMap.put(0x1f61a, R.drawable.emoji_1f61a);
        sEmojisMap.put(0x1f636, R.drawable.emoji_1f636);
        sEmojisMap.put(0x1f917, R.drawable.emoji_1f917);
        sEmojisMap.put(0x1f609, R.drawable.emoji_1f609);
        sEmojisMap.put(0x1f601, R.drawable.emoji_1f601);
        sEmojisMap.put(0x1f637, R.drawable.emoji_1f637);
        sEmojisMap.put(0x1f602, R.drawable.emoji_1f602);
        sEmojisMap.put(0x1f61d, R.drawable.emoji_1f61d);
        sEmojisMap.put(0x1f633, R.drawable.emoji_1f633);
        sEmojisMap.put(0x1f631, R.drawable.emoji_1f631);
        sEmojisMap.put(0x1f614, R.drawable.emoji_1f614);
        sEmojisMap.put(0x1f612, R.drawable.emoji_1f612);
        sEmojisMap.put(0x1f60c, R.drawable.emoji_1f60c);
        sEmojisMap.put(0x1f60f, R.drawable.emoji_1f60f);
        sEmojisMap.put(0x1f643, R.drawable.emoji_1f643);
        sEmojisMap.put(0x1f47d, R.drawable.emoji_1f47d);
        sEmojisMap.put(0x1f47b, R.drawable.emoji_1f47b);
        sEmojisMap.put(0x1f480, R.drawable.emoji_1f480);
        sEmojisMap.put(0x1f31a, R.drawable.emoji_1f31a);
        sEmojisMap.put(0x1f31d, R.drawable.emoji_1f31d);
        sEmojisMap.put(0x1f4a4, R.drawable.emoji_1f4a4);
        sEmojisMap.put(0x1f31e, R.drawable.emoji_1f31e);
        sEmojisMap.put(0x1f647, R.drawable.emoji_1f647);
        sEmojisMap.put(0x1f64b, R.drawable.emoji_1f64b);
        sEmojisMap.put(0x1f646, R.drawable.emoji_1f646);
        sEmojisMap.put(0x1f47e, R.drawable.emoji_1f47e);
        sEmojisMap.put(0x1f52a, R.drawable.emoji_1f52a);
        sEmojisMap.put(0x1f349, R.drawable.emoji_1f349);
        sEmojisMap.put(0x1f37b, R.drawable.emoji_1f37b);
        sEmojisMap.put(0x2615, R.drawable.emoji_2615);
        sEmojisMap.put(0x1f437, R.drawable.emoji_1f437);
        sEmojisMap.put(0x1f339, R.drawable.emoji_1f339);
        sEmojisMap.put(0x1f48b, R.drawable.emoji_1f48b);
        sEmojisMap.put(0x2764, R.drawable.emoji_2764);
        sEmojisMap.put(0x1f494, R.drawable.emoji_1f494);
        sEmojisMap.put(0x1f382, R.drawable.emoji_1f382);
        sEmojisMap.put(0x1f4a3, R.drawable.emoji_1f4a3);
        sEmojisMap.put(0x1f4a9, R.drawable.emoji_1f4a9);
        sEmojisMap.put(0x1f469, R.drawable.emoji_1f469);
        sEmojisMap.put(0x1f595, R.drawable.emoji_1f595);
        sEmojisMap.put(0x1f44d, R.drawable.emoji_1f44d);
        sEmojisMap.put(0x1f44e, R.drawable.emoji_1f44e);
        sEmojisMap.put(0x1f44f, R.drawable.emoji_1f44f);
        sEmojisMap.put(0x270c, R.drawable.emoji_270c);
        sEmojisMap.put(0x1f918, R.drawable.emoji_1f918);
        sEmojisMap.put(0x1f44a, R.drawable.emoji_1f44a);
        sEmojisMap.put(0x1f44c, R.drawable.emoji_1f44c);
        sEmojisMap.put(0x1f44b, R.drawable.emoji_1f44b);
        sEmojisMap.put(0x261d, R.drawable.emoji_261d);
        sEmojisMap.put(0x1f4aa, R.drawable.emoji_1f4aa);
        sEmojisMap.put(0x1f64f, R.drawable.emoji_1f64f);
        sEmojisMap.put(0x1f388, R.drawable.emoji_1f388);
        sEmojisMap.put(0x1f445, R.drawable.emoji_1f445);
        sEmojisMap.put(0x1f389, R.drawable.emoji_1f389);
        sEmojisMap.put(0x1f381, R.drawable.emoji_1f381);
        sEmojisMap.put(0x1f436, R.drawable.emoji_1f436);
        sEmojisMap.put(0x1f4b0, R.drawable.emoji_1f4b0);
        sEmojisMap.put(0x1f3b5, R.drawable.emoji_1f3b5);
        sEmojisMap.put(0x1f451, R.drawable.emoji_1f451);

        // Places
        sSoftbanksMap.put(0xe003, R.drawable.emoji_1f48b);
        sSoftbanksMap.put(0xe00e, R.drawable.emoji_1f44d);
        sSoftbanksMap.put(0xe010, R.drawable.emoji_270a);
        sSoftbanksMap.put(0xe011, R.drawable.emoji_270c);
        sSoftbanksMap.put(0xe022, R.drawable.emoji_2764);
        sSoftbanksMap.put(0xe023, R.drawable.emoji_1f494);
        sSoftbanksMap.put(0xe030, R.drawable.emoji_1f338);
        sSoftbanksMap.put(0xe032, R.drawable.emoji_1f339);
        sSoftbanksMap.put(0xe034, R.drawable.emoji_1f48d);
        sSoftbanksMap.put(0xe03c, R.drawable.emoji_1f3a4);
        sSoftbanksMap.put(0xe03e, R.drawable.emoji_1f3b5);
        sSoftbanksMap.put(0xe04e, R.drawable.emoji_1f47c);
        sSoftbanksMap.put(0xe056, R.drawable.emoji_1f60a);
        sSoftbanksMap.put(0xe057, R.drawable.emoji_1f603);
        sSoftbanksMap.put(0xe058, R.drawable.emoji_1f61e);
        sSoftbanksMap.put(0xe105, R.drawable.emoji_1f61c);
        sSoftbanksMap.put(0xe106, R.drawable.emoji_1f60d);
        sSoftbanksMap.put(0xe107, R.drawable.emoji_1f631);
        sSoftbanksMap.put(0xe108, R.drawable.emoji_1f613);
        sSoftbanksMap.put(0xe10c, R.drawable.emoji_1f47d);
        sSoftbanksMap.put(0xe10e, R.drawable.emoji_1f451);
        sSoftbanksMap.put(0xe112, R.drawable.emoji_1f381);
        sSoftbanksMap.put(0xe11a, R.drawable.emoji_1f47f);
        sSoftbanksMap.put(0xe11b, R.drawable.emoji_1f47b);
        sSoftbanksMap.put(0xe13c, R.drawable.emoji_1f4a4);
        sSoftbanksMap.put(0xe142, R.drawable.emoji_1f4e2);
        sSoftbanksMap.put(0xe14c, R.drawable.emoji_1f4aa);
        sSoftbanksMap.put(0xe306, R.drawable.emoji_1f490);
        sSoftbanksMap.put(0xe30c, R.drawable.emoji_1f37b);
        sSoftbanksMap.put(0xe310, R.drawable.emoji_1f388);
        sSoftbanksMap.put(0xe312, R.drawable.emoji_1f389);
        sSoftbanksMap.put(0xe326, R.drawable.emoji_1f3b6);
        sSoftbanksMap.put(0xe32e, R.drawable.emoji_2728);
        sSoftbanksMap.put(0xe32f, R.drawable.emoji_2b50);
        sSoftbanksMap.put(0xe331, R.drawable.emoji_1f4a6);
        sSoftbanksMap.put(0xe334, R.drawable.emoji_1f4a2);
        sSoftbanksMap.put(0xe34b, R.drawable.emoji_1f382);
        sSoftbanksMap.put(0xe401, R.drawable.emoji_1f625);
        sSoftbanksMap.put(0xe402, R.drawable.emoji_1f60f);
        sSoftbanksMap.put(0xe403, R.drawable.emoji_1f614);
        sSoftbanksMap.put(0xe404, R.drawable.emoji_1f601);
        sSoftbanksMap.put(0xe405, R.drawable.emoji_1f609);
        sSoftbanksMap.put(0xe406, R.drawable.emoji_1f623);
        sSoftbanksMap.put(0xe407, R.drawable.emoji_1f616);
        sSoftbanksMap.put(0xe408, R.drawable.emoji_1f62a);
        sSoftbanksMap.put(0xe40b, R.drawable.emoji_1f628);
        sSoftbanksMap.put(0xe40c, R.drawable.emoji_1f637);
        sSoftbanksMap.put(0xe40d, R.drawable.emoji_1f633);
        sSoftbanksMap.put(0xe40f, R.drawable.emoji_1f630);
        sSoftbanksMap.put(0xe410, R.drawable.emoji_1f632);
        sSoftbanksMap.put(0xe411, R.drawable.emoji_1f62d);
        sSoftbanksMap.put(0xe412, R.drawable.emoji_1f602);
        sSoftbanksMap.put(0xe413, R.drawable.emoji_1f622);
        sSoftbanksMap.put(0xe416, R.drawable.emoji_1f621);
        sSoftbanksMap.put(0xe417, R.drawable.emoji_1f61a);
        sSoftbanksMap.put(0xe418, R.drawable.emoji_1f618);
        sSoftbanksMap.put(0xe41d, R.drawable.emoji_1f64f);
        sSoftbanksMap.put(0xe41f, R.drawable.emoji_1f44f);
        sSoftbanksMap.put(0xe420, R.drawable.emoji_1f44c);
        sSoftbanksMap.put(0xe43c, R.drawable.emoji_1f302);
        sSoftbanksMap.put(0xe513, R.drawable.emoji_1f1e8_1f1f3);
    }

    public static final Emoji[] DATA = new Emoji[]{
            fromCodePoint(0x1f642),
            fromCodePoint(0x1f616),
            fromCodePoint(0x1f60d),
            fromCodePoint(0x1f62e),
            fromCodePoint(0x1f60e),
            fromCodePoint(0x1f62d),
            fromCodePoint(0x1f914),
            fromCodePoint(0x1f910),
            fromCodePoint(0x1f62a),
            fromCodePoint(0x1f629),
            fromCodePoint(0x1f628),
            fromCodePoint(0x1f621),
            fromCodePoint(0x1f61b),
            fromCodePoint(0x1f62c),
            fromCodePoint(0x1f627),
            fromCodePoint(0x1f641),
            fromCodePoint(0x1f624),
            fromCodePoint(0x1f61e),
            fromCodePoint(0x1f60a),
            fromCodePoint(0x1f644),
            fromCodePoint(0x1f60b),
            fromCodePoint(0x1f635),
            fromCodePoint(0x1f630),
            fromCodePoint(0x1f613),
            fromCodePoint(0x1f603),
            fromCodePoint(0x1f608),
            fromCodePoint(0x1f913),
            fromCodePoint(0x1f632),
            fromCodePoint(0x1f912),
            fromCodePoint(0x1f635),
            fromCodePoint(0x1f622),
            fromCodePoint(0x1f61c),
            fromCodePoint(0x1f61a),
            fromCodePoint(0x1f636),
            fromCodePoint(0x1f917),
            fromCodePoint(0x1f609),
            fromCodePoint(0x1f601),
            fromCodePoint(0x1f637),
            fromCodePoint(0x1f602),
            fromCodePoint(0x1f61d),
            fromCodePoint(0x1f633),
            fromCodePoint(0x1f631),
            fromCodePoint(0x1f614),
            fromCodePoint(0x1f612),
            fromCodePoint(0x1f60c),
            fromCodePoint(0x1f60f),
            fromCodePoint(0x1f643),
            fromCodePoint(0x1f47d),
            fromCodePoint(0x1f47b),
            fromCodePoint(0x1f480),
            fromCodePoint(0x1f31a),
            fromCodePoint(0x1f31d),
            fromCodePoint(0x1f4a4),
            fromCodePoint(0x1f31e),
            fromCodePoint(0x1f647),
            fromCodePoint(0x1f64b),
            fromCodePoint(0x1f646),
            fromCodePoint(0x1f47e),
            fromCodePoint(0x1f52a),
            fromCodePoint(0x1f349),
            fromCodePoint(0x1f37b),
            fromCodePoint(0x2615),
            fromCodePoint(0x1f437),
            fromCodePoint(0x1f339),
            fromCodePoint(0x1f48b),
            fromCodePoint(0x2764),
            fromCodePoint(0x1f494),
            fromCodePoint(0x1f382),
            fromCodePoint(0x1f4a3),
            fromCodePoint(0x1f4a9),
            fromCodePoint(0x1f469),
            fromCodePoint(0x1f595),
            fromCodePoint(0x1f44d),
            fromCodePoint(0x1f44e),
            fromCodePoint(0x1f44f),
            fromCodePoint(0x270c),
            fromCodePoint(0x1f918),
            fromCodePoint(0x1f44a),
            fromCodePoint(0x1f44c),
            fromCodePoint(0x1f44b),
            fromCodePoint(0x261d),
            fromCodePoint(0x1f4aa),
            fromCodePoint(0x1f64f),
            fromCodePoint(0x1f388),
            fromCodePoint(0x1f445),
            fromCodePoint(0x1f389),
            fromCodePoint(0x1f381),
            fromCodePoint(0x1f436),
            fromCodePoint(0x1f4b0),
            fromCodePoint(0x1f3b5),
            fromCodePoint(0x1f451),
            
    };



    public static Emoji fromCodePoint(int codePoint) {
        Emoji emoji = new Emoji();
        emoji.setEmoji(new String(Character.toChars(codePoint)));
        emoji.setIcon(getEmojiResId(codePoint, -1));
        emoji.setType(0);
        return emoji;
    }

    public static int getEmojiResId(int emoji, int notFoundId) {
        int resId = sEmojisMap.get(emoji, notFoundId);
        if (resId != notFoundId) {
            return resId;
        } else {
            resId = sSoftbanksMap.get(emoji, notFoundId);
            return resId;
        }

    }
}
