package br.com.vulcanogames.vgames360.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.vulcanogames.vgames360.R;
import com.asccode.tinyapi.model.Article;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 23/09/13
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class ListMainAdapter extends BaseAdapter {

    private final Context context;
    private final List<Article> articles;

    public ListMainAdapter( Context context, List<Article> articles) {

        this.context = context;
        this.articles = articles;

    }

    @Override
    public int getCount() {

        return articles.size();

    }

    @Override
    public Article getItem(int i) {
        return this.articles.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int i) {
        return this.getItem(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if( view == null )
            view = LayoutInflater.from(this.context).inflate(R.layout.lista_main_adapter, null);

        TextView itemTitle = (TextView) view.findViewById( R.id.itemTitle );
        TextView feedTitle = (TextView) view.findViewById( R.id.feedTitle );

        Typeface robotRegular = Typeface.createFromAsset( this.context.getAssets(), "Roboto-Regular.ttf" );
        Typeface robotBlackItalic = Typeface.createFromAsset( this.context.getAssets(), "Roboto-Regular.ttf" );

        if( itemTitle != null && feedTitle != null){

            itemTitle.setTypeface( robotRegular );
            feedTitle.setTypeface( robotBlackItalic );

            itemTitle.setText( this.getItem(i).getTitle() );
            feedTitle.setText( this.getItem(i).getFeedTitle() );

        }

        return view;

    }

}
