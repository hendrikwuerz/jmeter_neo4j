package com.poolingpeople.jmeter.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by hendrik on 20.05.15.
 */
public class DataGenerator {

    static Random rnd = new Random();
    static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String NUMBERS = "0123456789";

    static final String folder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/random_";
    static final String filePeople = folder + "users.csv";
    static final String fileTasks = folder + "tasks.csv";
    static final String fileNotes = folder + "notes.csv";
    static final String fileBugs = folder + "bugs.csv";
    static final String filePools = folder + "pools.csv";
    static final String fileTalks = folder + "talks.csv";
    static final String fileWorkspaces = folder + "workspaces.csv";
    static final String fileWorkspaceItems = folder + "workspaceItems.csv";
    static final String fileLinkedList = folder + "linkedList.csv";

    public static void main(String[] args) throws IOException {
        generatePeople(filePeople, 500);
        generateCoreTest(fileTasks, 2, fileTalks, 2, filePools, 2, fileNotes, 2, fileBugs, 3, filePeople);
        generateWorkspaceTest(fileWorkspaces, fileWorkspaceItems, 3, filePeople, fileTasks);
        generateLinkedList(fileLinkedList, 500);

        /*
        if(args.length < 1) return;

        if(args[0].equals("people")) generatePeople("/home/hendrik/dev/pooling-people/JMeter/Neo4j/random_users_java.csv", 300);
*/
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
            sb.append(new Person(i));
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
        Files.lines(new File(filename).toPath()).forEach(line -> list.add(new Person(line)));
        return list;
    }


    /**
     * returns a list of Tasks based on the csv file passed as argument
     *
     * @param filename
     *          The path of the csv file with the tasks
     * @return
     *          a list of Tasks based on the csv file
     * @throws IOException
     *          if reading file is not possible
     */
    private static Collection<Task> getCurrentTasks(String filename) throws IOException {
        LinkedList<Task> list = new LinkedList<Task>();
        Files.lines(new File(filename).toPath()).forEach(line -> list.add(new Task(line, true)));
        return list;
    }


    /**
     * generates the file for all workspaces and for all workspace items.
     * There will be as many workspaces as people exists in the passed file
     * There will be as many workspace items as tasks in the passed file
     *
     * @param filenameWorkspaces
     *          The filename to store workspaces
     * @param filenameWorkspaceItems
     *          The filename to store workspace items
     * @param amountPlaces
     *          How many workspace-item places will be needed for one workspace item (normally this is 3)
     * @param peopleFile
     *          The file where all people can be found
     * @param taskFile
     *          The file were all tasks can be found
     * @throws IOException
     */
    public static void generateWorkspaceTest(String filenameWorkspaces, String filenameWorkspaceItems, int amountPlaces, String peopleFile, String taskFile) throws IOException {
        // Create Workspaces for every person
        Collection<Person> people = getCurrentPeople(peopleFile);
        StringBuilder sb = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> sb.append(new Workspace(person.uuid)));
        PrintWriter out = new PrintWriter(filenameWorkspaces);
        out.print(sb);
        out.close();

        // Create Workspace Items for every task
        Collection<Task> tasks = getCurrentTasks(taskFile);
        StringBuilder sb2 = new StringBuilder(tasks.size() * 20);
        int[] idx = {0}; // index of current task
        int range = 5; // range of numbers of workspaceItems places
        int repeatAll = 100; // every repeatAll items it starts again with 0 for wsi place
        tasks.stream().forEach( task -> {sb2.append(new WorkspaceItem(task.uuid, amountPlaces, (idx[0]*range) % repeatAll, range)); idx[0]++; });
        out = new PrintWriter(filenameWorkspaceItems);
        out.print(sb2);
        out.close();
    }


    public static void generateCoreTest(String fileTasks, int tasksPerPerson, String fileTalks, int talksPerPerson, String filePools, int poolsPerPerson, String fileNotes, int notesPerPerson, String fileBugs, int bugsPerPerson, String filePeople) throws IOException {

        Collection<Person> people = getCurrentPeople(filePeople);

        // Create Note for every person
        StringBuilder sb = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < notesPerPerson; i++) {
                sb.append(new Note(person.uuid));
            }
        });
        PrintWriter out = new PrintWriter(fileNotes);
        out.print(sb);
        out.close();

        // Create Bug for every person
        StringBuilder sb2 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < bugsPerPerson; i++) {
                sb2.append(new Bug(person.uuid));
            }
        });
        out = new PrintWriter(fileBugs);
        out.print(sb2);
        out.close();

        // Create Pool for every person
        StringBuilder sb3 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < poolsPerPerson; i++) {
                sb3.append(new Pool(person.uuid));
            }
        });
        out = new PrintWriter(filePools);
        out.print(sb3);
        out.close();

        // Create Talk for every person
        StringBuilder sb4 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < talksPerPerson; i++) {
                sb4.append(new Talk(person.uuid));
            }
        });
        out = new PrintWriter(fileTalks);
        out.print(sb4);
        out.close();

        // Create Task for every person
        StringBuilder sb5 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < tasksPerPerson; i++) {
                sb5.append(new Task(person.uuid));
            }
        });
        out = new PrintWriter(fileTasks);
        out.print(sb5);
        out.close();
    }


    public static void generateLinkedList(String file, int amount) throws FileNotFoundException {

        StringBuilder sb = new StringBuilder(amount * 20);
        String last = "first-list-item-uuid";
        for(int i = 0; i < amount; i++) {
            LinkedListElement elem = new LinkedListElement(last, String.valueOf(i));
            sb.append(elem);
            last = elem.uuid;
        }
        PrintWriter out = new PrintWriter(file);
        out.print(sb);
        out.close();
    }




    public static String getRandomString(int length, String basic) {
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( basic.charAt( rnd.nextInt(basic.length()) ) );
        return sb.toString();
    }



}
