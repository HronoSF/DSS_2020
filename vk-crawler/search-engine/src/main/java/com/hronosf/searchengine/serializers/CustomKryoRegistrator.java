package com.hronosf.searchengine.serializers;

import com.esotericsoftware.kryo.Kryo;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import org.apache.spark.serializer.KryoRegistrator;

public class CustomKryoRegistrator implements KryoRegistrator {

    @Override
    public void registerClasses(Kryo kryo) {
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
    }
}