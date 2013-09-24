package br.com.vulcanogames.vgames360.menuSliding;

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

public class MenuSliding extends ListFragment {
	
	String[] list_contents = {"News", "Sobre"};
	
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

		switch (position){
		case 1:
            newFragment = new About();
			break;
		}

		if (newFragment != null)
			switchContent(newFragment);

	}
	
	private void switchContent(Fragment fragment){

		Main main = (Main)getActivity();
        main.switchContent(fragment);

	}

}
