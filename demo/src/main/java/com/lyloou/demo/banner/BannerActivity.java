/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.demo.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyloou.demo.R;
import com.lyloou.demo.common.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BannerActivity extends AppCompatActivity {
    Banner banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(getImageUrls());
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.startAutoPlay();
        banner.start();
    }
    public List<String> getImageUrls() {
        List<String> urls = new ArrayList<>();
        String[] strings = {
                "http://pic66.nipic.com/file/20150511/13629256_135451223000_2.jpg",
                "http://pic.58pic.com/58pic/13/87/72/73t58PICjpT_1024.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1922688573,3208736645&fm=23&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3449071175,3989458890&fm=23&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3259411197,509447914&fm=23&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=437889373,442789030&fm=23&gp=0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1798501903,126021128&fm=23&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2857815004,434566926&fm=23&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=987069714,37207753&fm=23&gp=0.jpg"};
        urls.addAll(Arrays.asList(strings));
        return urls;
    }
}