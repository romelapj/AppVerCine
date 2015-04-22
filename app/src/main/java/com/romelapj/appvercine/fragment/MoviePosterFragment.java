/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.romelapj.appvercine.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.romelapj.appvercine.R;
import com.romelapj.appvercine.activity.YoutubeActivity;
import com.squareup.picasso.Picasso;

/**
 * Fragment implementation created to show a poster inside an ImageView widget.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class MoviePosterFragment extends Fragment {


  private String posterTitle;
  private YoutubeActivity ya;
    @InjectView(R.id.buttonHorarios)
    Button buttonHorarios;
    @InjectView(R.id.tvTituloOriginal)
    TextView tvTituloOriginal;

  /**
   * Override method used to initialize the fragment.
   */
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_movie_poster, container, false);
      ButterKnife.inject(this,view);
      tvTituloOriginal.setText("Titulo Original: "+posterTitle);
      buttonHorarios.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              ya.minimizarVideo();
          }
      });
    return view;
  }

  /**
   * Show the poster image in the thumbnailImageView widget.
   */
  public void setPoster(String videoPosterThumbnail,YoutubeActivity ya) {
      this.ya=ya;
  }

  /**
   * Store the poster title to show it when the thumbanil view is clicked.
   */
  public void setPosterTitle(String posterTitle) {
    this.posterTitle = posterTitle;
  }



}