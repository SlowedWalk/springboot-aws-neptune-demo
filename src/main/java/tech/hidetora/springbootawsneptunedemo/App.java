package tech.hidetora.springbootawsneptunedemo;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

/**
 * @author hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */
public class App {
    public static void main( String[] args )
    {
        String sparqlEndpoint = "https://db-neptune-demo.cluster-c0egaleetogr.us-east-1.neptune.amazonaws.com:8182/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.init();

        try (RepositoryConnection conn = repo.getConnection()) {
            String queryString = "SELECT ?s ?p ?o WHERE { ?s ?p ?o } limit 10";

            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {  // iterate over the result
                    BindingSet bindingSet = result.next();

                    Value s = bindingSet.getValue("s");
                    Value p = bindingSet.getValue("p");
                    Value o = bindingSet.getValue("o");

                    System.out.print(s);
                    System.out.print("\t");
                    System.out.print(p);
                    System.out.print("\t");
                    System.out.println(o);
                }
            }
        }
    }
}
