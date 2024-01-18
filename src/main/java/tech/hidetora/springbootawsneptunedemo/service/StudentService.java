package tech.hidetora.springbootawsneptunedemo.service;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.UpdateExecutionException;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.stereotype.Service;
import tech.hidetora.springbootawsneptunedemo.entity.Student;

import java.util.UUID;

@Service
public class StudentService {

    public String createStudent(Student student) {
//        Repository repository = new HTTPRepository("https://db-neptune-demo.cluster-c0egaleetogr.us-east-1.neptune.amazonaws.com:8182/repositories/student-repo");
        String sparqlEndpoint = "https://db-neptune-demo.cluster-c0egaleetogr.us-east-1.neptune.amazonaws.com:8182/sparql";
        Repository repo = new SPARQLRepository(sparqlEndpoint);
        repo.init();
        try (RepositoryConnection connection = repo.getConnection()) {
            String sparqlInsert = "INSERT DATA { "
                    + "<urn:student:" + UUID.randomUUID() + "> <urn:name>" + student.getName() + " ; "
                    + "                     <urn:email>" + student.getEmail() + " . } "
                    + "                     <urn:phone>" + student.getPhone() + " . } "
                    + "                     <urn:address>" + student.getAddress() + " . } ";
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, sparqlInsert);
            update.execute();
            return "Student created successfully";
        } catch (Exception e) {
            return "Error creating student";
        }
    }

    public String readStudent(String id) {
        Repository repository = new HTTPRepository("https://db-neptune-demo.cluster-c0egaleetogr.us-east-1.neptune.amazonaws.com:8182/repositories/student-repo");

        try (RepositoryConnection connection = repository.getConnection()) {
            String sparqlSelect = "SELECT ?name ?email WHERE { "
                    + "<urn:student:john> <urn:name> ?name ; "
                    + "                     <urn:email> ?email . }";
            GraphQueryResult statements = connection.prepareGraphQuery(QueryLanguage.SPARQL, sparqlSelect).evaluate();
            String result = null;
            for (Statement statement : statements) {
                // Process the results
                result = statement.getObject().stringValue();
            }
            return result;
        } catch (Exception e) {
            return "Error reading student";
        }
    }

    public String readAllStudents() {
        Repository repository = new HTTPRepository("https://db-neptune-demo.cluster-c0egaleetogr.us-east-1.neptune.amazonaws.com:8182/repositories/student-repo");

        try (RepositoryConnection connection = repository.getConnection()) {
            String sparqlSelect = "SELECT ?subject ?predicate ?object" +
                    "        FROM <urn:default-graph>" +
                    "                WHERE {" +
                    "            GRAPH ?g {" +
                    "    ?subject ?predicate ?object" +
                    "            }" +
                    "        }";
            GraphQueryResult statements = connection.prepareGraphQuery(QueryLanguage.SPARQL, sparqlSelect).evaluate();
            String result = null;
            for (Statement statement : statements) {
                // Process the results
                result = statement.getObject().stringValue();
            }
            return result;
        } catch (Exception e) {
            return "Error reading student";
        }

    }

//    public String updateStudent(Long id, Student student) {
//
//    }

    public String deleteStudent(Long id) {
        Repository repository = new HTTPRepository("https://db-neptune-demo.cluster-c0egaleetogr.us-east-1.neptune.amazonaws.com:8182/repositories/student-repo");
        try (RepositoryConnection connection = repository.getConnection()) {
            String sparqlDelete = "DELETE WHERE {"
                    + "  <urn:student:john> \"valueToDelete\" .\n" +
                    "}";
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, sparqlDelete);
            update.execute();
            return "Student deleted successfully";
        } catch (UpdateExecutionException e) {
            e.printStackTrace();
            return "Error deleting student";
        }
    }
}
