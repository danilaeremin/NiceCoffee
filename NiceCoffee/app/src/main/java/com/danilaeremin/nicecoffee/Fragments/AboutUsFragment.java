package com.danilaeremin.nicecoffee.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.danilaeremin.nicecoffee.AboutUsActivity;
import com.danilaeremin.nicecoffee.Adapters.CategoryGridAdapter;
import com.danilaeremin.nicecoffee.Adapters.DataAdapter;
import com.danilaeremin.nicecoffee.MainActivity;
import com.danilaeremin.nicecoffee.R;
import com.danilaeremin.nicecoffee.core.Utils;

/**
 * Created by danilaeremin on 09.03.15.
 */
public class AboutUsFragment extends Fragment {
    private static final String LOG_TAG = AboutUsFragment.class.getSimpleName();

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    public AboutUsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.about_us);
        String str = "<center><p></p>\n" +
                "<p></p>\n" +
                "<p><span style=\"font-size: large;\"><strong><br /></strong></span></p>\n" +
                "<p><span style=\"font-size: large;\"><strong>Уважаемые Дамы и Господа, приветствуем Вас в интернет-магазине </strong></span></p>\n" +
                "<p><span style=\"font-size: large;\"><strong><br /></strong></span></p>\n" +
                "<p><span style=\"font-size: large;\"><strong>NiceCoffee.ru!</strong></span></p>\n" +
                "</center>" +
                "<p></p>\n" +
                "<p></p>\n" +
                "<p></p>\n" +
                "<p>      </p>\n" +
                "<p>       Для ценителей поистине прекрасного, компания НайсКофе предлагает Вам широкий ассортимент товаров молотого кофе и кофе в зернах ведущих мировых производителей <strong>Lavazza, Pellini, Milani, Paulig</strong> и многих других, кофемашины высокой прочности <strong>Jura, Saeco, Gaggia,</strong> сиропы для кофе и кондитерских изделий без красителей <strong>Teisseire и Vedrenne</strong>, а также немецкий чай <strong>Althaus, Messmer</strong> и расходные материалы. Даже самый требовательный клиент не останется без покупки.</p>\n" +
                "<p></p>\n" +
                "<p></p>\n" +
                "<p></p>\n" +
                "<p>       </p>\n" +
                "<p></p>\n" +
                "<p>Компания NiceCoffee сосредоточена на реализации лучших в мире сортов кофе и современного оборудования. Мы ориентированы на наших клиентов, для каждого покупателя у нас свой индивидуальный подход.</p>\n" +
                "<p><br />Миссия компании состоит в том, чтобы сделать процесс покупки удобным и легким для Вас, чтобы наши клиенты получали наслаждение от каждой покупки. Для этого мы используем новейшие технологии для обеспечения простоты выбора товара и предотвращения неудобств клиента при заказе.</p>\n" +
                "<p></p>\n" +
                "<p>     </p>\n" +
                "<p>Мы провели тщательное маркетинговое исследование и выбрали самые популярные позиции товаров, сформировали доступные цены и разработали уникальные предложения для Вас. Специалисты нашей компании имеют огромный опыт в сфере продаж кофейной тематики. Мы без труда поможем в интересующем Вас вопросе, проконсультируем относительно критерия цена - качество, что существенно сократит Ваше драгоценное время. Приобрести кофе в нашей компании удобно, быстро и просто.</p>\n" +
                "<p></p>\n" +
                "<p>     </p>\n" +
                "<p>Мы ценим Ваше внимание, и если Вы обнаружили товар по более выгодной цене, мы готовы сделать предложение лучше. Индивидуальный подход к каждому клиенту!</p>\n" +
                "<p></p>\n" +
                "<p></p>\n" +
                "<center><p><br /><strong>С уважением, коллектив компании NiceCoffee!</strong></p></center>";
        textView.setText(Html.fromHtml(str));

        return rootView;
    }
}