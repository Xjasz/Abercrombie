package com.jaszapps.abercrombie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import core.Singleton;
import core.reflection.injectors.Inject;
import core.reflection.listeners.Listen;
import core.reflection.messages.ActionBarMessage;
import core.reflection.messages.BackRequestMessage;
import core.reflection.messages.NetworkMessage;
import core.reflection.models.BaseFragment;
import core.webservice.WebService;


public class FragmentMain extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    private MainAdapter adapter;


    @Listen(consumed = true)
    public void onMessageReceived(BackRequestMessage<Integer> request) {
        switch (request.object) {
            case 0:
                request.respond(0);
                break;
        }
    }

    @Inject(id = android.R.id.list)
    private ListView listview;

    @Inject(id = android.R.id.empty)
    private View emptyView;

    @Inject(id = R.id.loadingSpinner)
    private ProgressBar loadingSpinner;

    @Inject(id = R.id.tvEmptyText)
    private TextView tvEmptyText;

    @Inject(id = R.id.ivEmptyText)
    private View ivEmptyText;

    @Override
    public int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = super.onCreateView(inflater, container, savedInstanceState);
        initializeFragment();
        return viewGroup;
    }

    @Override
    public void onDestroyView() {
        this.hideSoftKeyboard();
        super.onDestroyView();

        if (listview != null) {
            listview.smoothScrollBy(0, 0);
        }
        if (Singleton.AdapterItems != null) {
            Singleton.AdapterItems.clear();
        }
    }

    @Listen()
    public void onNetworkMessageReceived(NetworkMessage<Integer> request) {
        if (!request.response.contentEquals(WebService.VALID)) {
            Singleton.toastMessage(request.response);
        } else {
            switch (request.object) {
                case WebService.GET_ABERCROMBIE_DATA:
                    Log.d(TAG, "got data");
                    if (Singleton.AdapterItems.isEmpty()) {
                        ivEmptyText.setVisibility(View.VISIBLE);
                        tvEmptyText.setVisibility(View.VISIBLE);
                    } else {
                        ivEmptyText.setVisibility(View.GONE);
                        tvEmptyText.setVisibility(View.GONE);
                        loadingSpinner.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }


    @Listen()
    public void onActionBarMessageReceived(final ActionBarMessage<Integer> request) {
        switch (request.id) {
            case 3:
                Log.d(TAG, "REFRESHING");
                Singleton.AdapterItems.clear();
                adapter.notifyDataSetChanged();
                loadingSpinner.setVisibility(View.VISIBLE);
                new WebService().webEvent(new int[]{WebService.GET, WebService.GET_ABERCROMBIE_DATA}, null);
                break;
        }
    }


    private void initializeFragment() {
        adapter = new MainAdapter();
        listview.setAdapter(adapter);

        if (Singleton.AdapterItems.isEmpty()) {
            loadingSpinner.setVisibility(View.VISIBLE);
        } else {
            loadingSpinner.setVisibility(View.GONE);
        }

        new WebService().webEvent(new int[]{WebService.GET, WebService.GET_ABERCROMBIE_DATA}, null);
    }

    public class MainAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        public MainAdapter() {
            layoutInflater = LayoutInflater.from(Singleton.getMainActivity());
        }

        @Override
        public int getCount() {
            return Singleton.AdapterItems.size();
        }

        @Override
        public Object getItem(int position) {
            try {
                return Singleton.AdapterItems.get(position);
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return ((ItemMain) getItem(position)).getView(position, layoutInflater, convertView, parent);
        }
    }
}
