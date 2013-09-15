package br.com.vulcanogames.gamepoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.vulcanogames.gamepoint.telas.Main;
import br.com.vulcanogames.gamepoint.telas.MainView;

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
		FragmentManager fm = getFragmentManager();

		switch (position){
		case 0:
			break;
		}

		if (newFragment != null)
			switchContent(newFragment);

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

	}
	
	private void switchContent(Fragment fragment){

		Main main = (Main)getActivity();
        main.switchContent(fragment);

	}

}
