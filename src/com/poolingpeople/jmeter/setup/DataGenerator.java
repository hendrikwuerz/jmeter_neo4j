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

    static final String folder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/data/";
    static final String filePrefix = "random_";
    static final int amountServers = 5;

    static final String filePeople = "users.csv";
    static final String fileTasks = "tasks.csv";
    static final String fileNotes = "notes.csv";
    static final String fileBugs = "bugs.csv";
    static final String filePools = "pools.csv";
    static final String fileTalks = "talks.csv";
    static final String fileWorkspaces = "workspaces.csv";
    static final String fileWorkspaceItems = "workspaceItems.csv";
    static final String fileLinkedList = "linkedList.csv";
    static final String fileLinkedListCompare = "linkedListCompare.csv";
    static final String fileLinkedListCompareShort = "linkedListCompareShort.csv";
    static final String fileStuffElement = "stuffElement.csv";
    static final String fileStuffRelation = "stuffRelation.csv";

    public static void main(String[] args) throws IOException {
        for(int i = 0; i < amountServers; i++) {
            File dir = new File(folder + (i+1));
            dir.mkdirs();
            String pre = folder + (i+1) + "/" + filePrefix;

            generatePeople(pre+filePeople, i, 8000);
            generateCoreTest(pre+fileTasks, 2, pre+fileTalks, 2, pre+filePools, 2, pre+fileNotes, 2, pre+fileBugs, 3, pre+filePeople);
            generateWorkspaceTest(pre+fileWorkspaces, pre+fileWorkspaceItems, 3, pre+filePeople, pre+fileTasks);
            generateLinkedList(pre+fileLinkedList, pre+fileLinkedListCompare, pre+fileLinkedListCompareShort, 200);
            generateStuff(pre+fileStuffElement, pre+fileStuffRelation, 5, 2);
        }
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
    public static void generatePeople(String filename, int filenumber, int amount) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder( 150 * amount );
        for(int i = 0; i < amount; i++) {
            sb.append(new Person(filenumber * amount + i));
        }
        saveIn(filename, sb);

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
        saveIn(filenameWorkspaces, sb);

        // Create Workspace Items for every task
        Collection<Task> tasks = getCurrentTasks(taskFile);
        StringBuilder sb2 = new StringBuilder(tasks.size() * 20);
        int[] idx = {0}; // index of current task
        int range = 5; // range of numbers of workspaceItems places
        int repeatAll = 100; // every repeatAll items it starts again with 0 for wsi place
        tasks.stream().forEach( task -> {sb2.append(new WorkspaceItem(task.uuid, amountPlaces, (idx[0]*range) % repeatAll, range)); idx[0]++; });
        saveIn(filenameWorkspaceItems, sb2);
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
        saveIn(fileNotes, sb);

        // Create Bug for every person
        StringBuilder sb2 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < bugsPerPerson; i++) {
                sb2.append(new Bug(person.uuid));
            }
        });
        saveIn(fileBugs, sb2);

        // Create Pool for every person
        StringBuilder sb3 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < poolsPerPerson; i++) {
                sb3.append(new Pool(person.uuid));
            }
        });
        saveIn(filePools, sb3);

        // Create Talk for every person
        StringBuilder sb4 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < talksPerPerson; i++) {
                sb4.append(new Talk(person.uuid));
            }
        });
        saveIn(fileTalks, sb4);

        // Create Task for every person
        StringBuilder sb5 = new StringBuilder(people.size() * 20);
        people.stream().forEach( person -> {
            for(int i = 0; i < tasksPerPerson; i++) {
                sb5.append(new Task(person.uuid));
            }
        });
        saveIn(fileTasks, sb5);
    }


    public static void generateLinkedList(String file, String fileCompare, String fileCompareShort, int amount) throws FileNotFoundException {

        LinkedList<LinkedListElement> list = new LinkedList<>();

        StringBuilder sb = new StringBuilder(amount * 20);
        String last = "first-list-item-uuid";
        for(int i = 0; i < amount; i++) {
            LinkedListElement elem = new LinkedListElement(last, String.valueOf(i));
            list.add(elem);
            sb.append(elem);
            last = elem.uuid;
        }
        saveIn(file, sb);

        // Long check
        sb = new StringBuilder(amount * 20);
        for(int i = 0; i < amount; i++) {
            Random rand = new Random();
            int elem1 = rand.nextInt(list.size());
            int elem2 = rand.nextInt(list.size());

            //sb.append(list.get(elem1).uuid).append(",").append(list.get(elem2).uuid).append(",").append(elem1 < elem2 ? "data\":[{\"row\":[" : "data\":[]").append(System.lineSeparator());
            sb.append(list.get(elem1).uuid).append(",").append(list.get(elem2).uuid).append(",").append(elem1 < elem2 ? "true" : "false").append(",").append(elem2 - elem1).append(System.lineSeparator());
        }
        saveIn(fileCompare, sb);

        // short check
        sb = new StringBuilder(amount * 20);
        for(int i = 0; i < amount; i++) {
            Random rand = new Random();
            int elem1 = rand.nextInt(list.size());
            int elem2 = Math.min(elem1 + 5 - rand.nextInt(11), list.size() - 1);
            if(elem2 < 0 || elem1 == elem2) {
                i--; //try again
            } else {
                sb.append(list.get(elem1).uuid).append(",").append(list.get(elem2).uuid).append(",").append(elem1 < elem2 ? "true" : "false").append(",").append(elem2 - elem1).append(System.lineSeparator());
            }
        }
        saveIn(fileCompareShort, sb);
    }

    public static void generateStuff(String fileStuffElement, String fileStuffRelation, int amount, int deep) throws FileNotFoundException {
        Star root = new Star(amount, deep);

        saveIn(fileStuffElement, root.getElement());
        saveIn(fileStuffRelation, root.getRelation());
    }

    private static void saveIn(String filename, StringBuilder sb) throws FileNotFoundException {
        saveIn(filename, sb.toString());
    }

    private static void saveIn(String filename, String sb) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(filename);
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
