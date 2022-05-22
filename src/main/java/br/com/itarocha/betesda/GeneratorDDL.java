package br.com.itarocha.betesda;

//import org.jooq.meta.Database;
//import org.hibernate.dialect.Database;
//import org.hibernate.dialect.Database;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.Target;

public class GeneratorDDL {
    public static void main(String[] args) {
        var cfg =

        new org.jooq.meta.jaxb.Configuration()
                .withGenerator(new Generator()
                        .withTarget(new Target()
                                .withDirectory("target/oua-generated-sources/jooq")
                        )
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                                .withProperties(
// Specify the location of your SQL script.
// You may use ant-style file matching, e.g. /path/**/to/*.sql
//
// Where:
// - ** matches any directory subtree
// - * matches any number of characters in a directory / file name
// - ? matches a single character in a directory / file name
                                        new Property()
                                                .withKey("scripts")
                                                .withValue("src/main/resources/betesda.sql"),
// The sort order of the scripts within a directory, where:
//
// - semantic: sorts versions, e.g. v-3.10.0 is after v-3.9.0 (default)
// - alphanumeric: sorts strings, e.g. v-3.10.0 is before v-3.9.0
// - flyway: sorts files the same way as flyway does
// - none: doesn't sort directory contents after fetching them from the directory
                                        new Property()
                                                .withKey("sort")
                                                .withValue("semantic"),
// The default schema for unqualified objects:
//
// - public: all unqualified objects are located in the PUBLIC (upper case) schema
// - none: all unqualified objects are located in the default schema (default)
//
// This configuration can be overridden with the schema mapping feature
                                        new Property()
                                                .withKey("unqualifiedSchema")
                                                .withValue("none"),
// The default name case for unquoted
//
// - as_is: unquoted object names are
// - upper: unquoted object names are
// - lower: unquoted object names are
                                        new Property()
                                                .withKey("defaultNameCase")
                                                .withValue("as_is")
)
)
);

    }
}
