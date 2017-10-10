package com.example.alaazuhour.bakery.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alaazuhour.bakery.R;
import com.example.alaazuhour.bakery.adapter.StepAdapter;
import com.example.alaazuhour.bakery.model.Recipe;
import com.example.alaazuhour.bakery.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeStepDetailFragment extends Fragment {
    private ArrayList<Step> stepArrayList;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private long playerPostion;
    private int index;
    private  Recipe recipe;
    private OnButtonClickListener mListener;
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;
    private String videoURL;
    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    public long getPlayerPostion() {
        return playerPostion;
    }

    public void setPlayerPostion(long playerPostion) {
        this.playerPostion = playerPostion;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        if(savedInstanceState == null) {
            recipe = getActivity().getIntent().getExtras().getParcelable("recipe");
            if(getArguments() != null)
              index = getArguments().getInt("selectd");
            else
                index=0;
        }else{
           recipe = savedInstanceState.getParcelable("recipe");
            index = savedInstanceState.getInt("selectd");

        }
        stepArrayList = (ArrayList<Step>) recipe.getSteps();
        videoURL = stepArrayList.get(index).getVideoURL();
        TextView textView = (TextView) rootView.findViewById(R.id.step_description);
        textView.setText(stepArrayList.get(index).getDescription());
        String imageUrl=stepArrayList.get(index).getThumbnailURL();
        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.imageView2);
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }
        if (!videoURL.isEmpty()) {
            initializePlayer(Uri.parse(videoURL));
            player.seekTo(playerPostion);
            if(rootView.findViewWithTag("layout-land") != null){
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            }

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail")!=null) {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
//                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            }
            simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

        } else {
            player=null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }
        Button mPrevStep = (Button) rootView.findViewById(R.id.button2);
        Button mNextstep = (Button) rootView.findViewById(R.id.button);

        mPrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (stepArrayList.get(index).getId() > 0) {
                    if (player!=null){
                        player.stop();
                    }
                       mListener.onButtonClick(index-1);
                }
                else {
                    Toast.makeText(getActivity(),"You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }});

        mNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int lastIndex = stepArrayList.size()-1;
                if (stepArrayList.get(index).getId() < stepArrayList.get(lastIndex).getId()) {
                    if (player!=null){
                        player.stop();
                    }
                    mListener.onButtonClick(index+1);
                }
                else {
                    Toast.makeText(getContext(),"You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }});
        return rootView;
    }

    private void initializePlayer(Uri parse) {
        if (player == null) {
            mainHandler = new Handler();
            bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);
            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            mListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player!=null) {
            player.stop();
            player.release();
            player=null;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectd",index);
        outState.putParcelable("recipe",recipe);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player!=null) {
            playerPostion = player.getCurrentPosition();
            player.stop();
            player.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnButtonClickListener {
        void onButtonClick(int i);
    }
}
