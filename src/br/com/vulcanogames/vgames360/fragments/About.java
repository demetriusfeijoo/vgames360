package br.com.vulcanogames.vgames360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.vulcanogames.vgames360.R;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 16/09/13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class About extends SherlockFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.about, null);
    }
}