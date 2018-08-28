package sql2o.extensions.quirks;
import sql2o.quirks.Quirks;
import sql2o.quirks.QuirksProvider;

/**
 * Created by lars on 28.10.14.
 */
public class PostgresQuirksProvider implements QuirksProvider {


    @Override
    public Quirks provide() {
        return new PostgresQuirks();
    }

    @Override
    public boolean isUsableForUrl(String url) {
        return url.startsWith("jdbc:postgresql:");
    }

    @Override
    public boolean isUsableForClass(String className) {
        return className.startsWith("org.postgresql.");
    }
}
