package unity.util;

import arc.assets.*;
import arc.assets.loaders.*;
import arc.files.*;
import arc.struct.*;
import arc.util.*;

@SuppressWarnings("rawtypes")
public class WavefrontObjectLoader extends AsynchronousAssetLoader<WavefrontObject, WavefrontObjectLoader.WavefrontObjectParameters>{
    private WavefrontObject object;

    public WavefrontObjectLoader(FileHandleResolver resolver){
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager assetManager, String fileName, Fi file, WavefrontObjectParameters parameter){
        if(parameter != null && parameter.object != null){
            (object = parameter.object).load(file);
        }else{
            object = new WavefrontObject();
            object.load(file);
        }
    }

    @Override
    public WavefrontObject loadSync(AssetManager assetManager, String fileName, Fi file, WavefrontObjectParameters parameter){
        WavefrontObject object = this.object;
        this.object = null;
        return object;
    }

    @Override
	public Seq<AssetDescriptor> getDependencies(String fileName, Fi file, WavefrontObjectParameters parameter){
		return null;
	}

    public static class WavefrontObjectParameters extends AssetLoaderParameters<WavefrontObject>{
        public @Nullable WavefrontObject object;

        public WavefrontObjectParameters(){}

        public WavefrontObjectParameters(@Nullable WavefrontObject object){
            this.object = object;
        }
    }
}
