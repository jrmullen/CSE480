package com.watchdog.dao.video;

import com.watchdog.business.Video;
import java.util.List;

/**
 * Created by theNextThing on 10/12/2016.
 */

public interface VideoDao {

    //List<Video> listVideos();

    //Create
    void save(Video video);

    Video getByVideoTitle(String videoTitle);

    //Read
    Video getByVidId(int id);

    //Update
    void update(Video video);

    //Delete
    void deleteByVidId(int id);

    void deleteByName(String videoName);

    //Get All
    List<Video> getAll();
}

