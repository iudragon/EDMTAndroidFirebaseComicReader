package dragon.bakuman.iu.edmtandroidfirebasecomicreader.Interface;

import java.util.List;

import dragon.bakuman.iu.edmtandroidfirebasecomicreader.Model.Comic;

public interface IComicLoadDone {

    void onComicLoadDoneListener(List<Comic> comicList);
}
