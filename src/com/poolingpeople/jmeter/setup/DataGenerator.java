package com.poolingpeople.jmeter.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by hendrik on 20.05.15.
 */
public class DataGenerator {

    static Random rnd = new Random();
    static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String NUMBERS = "0123456789";

    public static void main(String[] args) throws FileNotFoundException {
        //generatePeople("/home/hendrik/dev/pooling-people/JMeter/Neo4j/random_users_java.csv", 100000);
        generateTasks("/home/hendrik/dev/pooling-people/JMeter/Neo4j/random_tasks_java.csv", 100000);

        /*
        if(args.length < 1) return;

        if(args[0].equals("people")) generatePeople("/home/hendrik/dev/pooling-people/JMeter/Neo4j/random_users_java.csv", 300);
*/
    }


    /**
     * Create a random csv file with tasks
     *
     * @param filename
     *          The path of the csv file for the tasks
     * @param amount
     *          The amount of tasks to be created
     * @throws FileNotFoundException
     */
    public static void generateTasks(String filename, int amount) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder( 90 * amount );
        for(int i = 0; i < amount; i++) {
            long now1 = System.currentTimeMillis() - (long)(Math.random() * 1000000000);
            long now2 = now1 + (long)(Math.random() * 1000);
            String title = "My task title " + i;
            String type = "task";
            String uuid = UUID.randomUUID().toString();

            sb.append(now1).append(",");
            sb.append(now2).append(",");
            sb.append(title).append(",");
            sb.append(type).append(",");
            sb.append(uuid).append(System.lineSeparator());
        }

        PrintWriter out = new PrintWriter(filename);
        out.print(sb);
        out.close();
    }


    /**
     * create a random csv file with people
     *
     * @param filename
     *          The path of the csv file for the people
     * @param amount
     *          The amount of people to be created
     * @throws FileNotFoundException
     */
    public static void generatePeople(String filename, int amount) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder( 150 * amount );
        for(int i = 0; i < amount; i++) {
            sb.append(new Person(i).toString()).append(System.lineSeparator());
        }
        PrintWriter out = new PrintWriter(filename);
        out.print(sb);
        out.close();

    }

    /**
     * returns a list of Persons based on the csv file passed as argument
     *
     * @param filename
     *          The path of the csv file with the people
     * @return
     *          a list of Persons based on the csv file
     * @throws IOException
     *          if reading file is not possible
     */
    private static Collection<Person> getCurrentPeople(String filename) throws IOException {
        LinkedList<Person> list = new LinkedList<Person>();
        Files.lines(new File(filename).toPath()).forEach( line -> list.add(new Person(line)));
        return list;
    }


    public static void generateTestGetWorkspaceItems(String filename, int amount, String peopleFile) throws FileNotFoundException {

    }




    public static String getRandomString(int length, String basic) {
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( basic.charAt( rnd.nextInt(basic.length()) ) );
        return sb.toString();
    }



}
