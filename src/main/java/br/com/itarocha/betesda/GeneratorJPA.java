package br.com.itarocha.betesda;

//import org.jooq.meta.Database;
//import org.hibernate.dialect.Database;
//import org.hibernate.dialect.Database;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Property;

public class GeneratorJPA {

    // https://blog.jooq.org/tag/ddl-simulation/
    /*

// Fetch a SQL string from a jOOQ Query in order to manually execute it with another tool.
Query query = create.select(BOOK.TITLE, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                    .from(BOOK)
                    .join(AUTHOR)
                    .on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                    .where(BOOK.PUBLISHED_IN.eq(1948));

String sql = query.getSQL();
List<Object> bindValues = query.getBindValues();

String sql = query.getSQL(ParamType.INLINED);

https://www.jooq.org/doc/3.15/manual/getting-started/tutorials/jooq-in-7-steps/jooq-in-7-steps-step3/


     */



    public static void main(String[] args) {
        new org.jooq.meta.jaxb.Configuration()
                .withGenerator(new Generator()
                                .withDatabase(new Database()
                                                .withName("org.jooq.meta.extensions.jpa.JPADatabase")
                                                .withProperties(
// A comma separated list of Java packages, that contain your entities
                                                        new Property()
                                                                .withKey("packages")
                                                                .withValue("br.com.itarocha.betesda.adapter.out.persistence.jpa.entity"),
// Whether JPA 2.1 AttributeConverters should be auto-mapped to jOOQ Converters.
// Custom <forcedType/> configurations will have a higher priority than these auto-mapped converters.
// This defaults to true.
                                                        new Property()
                                                                .withKey("useAttributeConverters")
                                                                .withValue("true"),
// The default schema for unqualified objects:
//
// - public: all unqualified objects are located in the PUBLIC (upper case) schema
// - none: all unqualified objects are located in the default schema (default)
//
// This configuration can be overridden with the schema mapping feature
                                                        new Property()
                                                                .withKey("unqualifiedSchema")
                                                                .withValue("none")
                                                )
                                )
                );
    }
}
