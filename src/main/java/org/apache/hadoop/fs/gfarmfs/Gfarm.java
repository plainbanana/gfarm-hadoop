package org.apache.hadoop.fs.gfarmfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.DelegateToFileSystem;
import org.apache.hadoop.fs.gfarmfs.GfarmFileSystem;

import java.io.*;
import java.net.*;

/**
* Gfarm implementation of AbstractFileSystem. This impl delegates to the
* GfarmFileSystem
*/
public class Gfarm extends DelegateToFileSystem {

    public Gfarm(URI theUri, Configuration conf) throws IOException, URISyntaxException {
        super(theUri, new GfarmFileSystem(), conf, "gfarm", false);
    }

    @Override
    public int getUriDefaultPort() {
        return super.getUriDefaultPort();
    }
}
