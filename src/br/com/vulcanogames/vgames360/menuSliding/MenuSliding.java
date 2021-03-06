package br.com.vulcanogames.vgames360.menusliding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.vulcanogames.vgames360.R;
import br.com.vulcanogames.vgames360.fragments.About;
import br.com.vulcanogames.vgames360.activities.Main;
import br.com.vulcanogames.vgames360.fragments.MainView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class MenuSliding extends ListFragment {
	
	String[] list_contents = {"Notícias", "Game Versus", "Sobre"};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		return inflater.inflate(R.layout.sliding_menu, container, false);

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){

		super.onActivityCreated(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_contents));

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){

		Fragment newFragment = new MainView();

        EasyTracker easyTracker = EasyTracker.getInstance(getActivity());


		switch (position){
        case 1:
            newFragment = new About();
            break;
		case 2:
            newFragment = new About();
			break;
		}

		if (newFragment != null){

            switchContent(newFragment);

            if( position+1 <= list_contents.length )
                easyTracker.send(MapBuilder
                        .createEvent(
                                "ui_action",            // Event category (required)
                                "click",                // Event action (required)
                                " Menu Item: "+list_contents[position],     // Event label
                                null)                   // Event value
                        .build()
                );
        }

	}
	
	private void switchContent(Fragment fragment){

		Main main = (Main)getActivity();
        main.switchContent(fragment);

	}

}
