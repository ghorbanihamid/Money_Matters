import java.io.*;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Hamid Ghorbani on 08/27/2015.
 */

public class MoneyMatters {

    static final int maxFriendsCount = 10000;
    static final int maxFriendshipCount = 50000;

    static String fileName = "MoneyMatters.txt";
    static String fileData[];

    static int friendsCount;
    static int friendshipsCount;

    static Vector peopleVector;
    static int[][] friendshipList;

    MoneyMatters(int friendsCount, int friendshipsCount, Vector peopleVectorList,int[][] friendshipArrayList){
        this.friendsCount     = friendsCount;
        this.friendshipsCount = friendshipsCount;
        this.peopleVector     = peopleVectorList;
        this.friendshipList   = friendshipArrayList;
    }

    /* We put people's Info(name && money) in peopleVector
     * Each friendship has 2 friends, so for each friend in friendship List
     * we search peopleVector and put these 2 person in 2 different node.
     * then we merge these 2 person and create one object from them and delete the second one.
     * if subtraction of these 2 person was zero then, they can clear their debt, so we can delete both objects.
     * at the end if all people's owe or owed were zero, clearing Debts is POSSIBLE.
     */
    public void exploreMoneyProblem() {

        try {
            for(int i = 0; i < friendshipsCount; i++){

                PersonDto tempPersonDto;
                PersonDto personNode1 = null;
                PersonDto personNode2 = null;

                String friend1 = String.valueOf(this.friendshipList[i][0]);
                String friend2 = String.valueOf(this.friendshipList[i][1]);
                for (Object personObject : peopleVector) {
                    tempPersonDto = (PersonDto) personObject;
                    for (String friendName : tempPersonDto.getPersonName().split(",")) {
                        if (friendName.equals(friend1)) {
                            personNode1 = tempPersonDto;
                            break;
                        }
                        else if (friendName.equals(friend2)) {
                            personNode2 = tempPersonDto;
                            break;
                        }
                    }
                    if (personNode1 != null && personNode2 != null) {
                        break;
                    }
                }
                long sumAmount = personNode1.getPersonOwe() + personNode2.getPersonOwe();
                if(sumAmount == 0){
                    peopleVector.remove(personNode1);
                }
                else {
                    personNode1.setPersonName(personNode1.getPersonName() + "," + personNode2.getPersonName());
                    personNode1.setPersonOwe(sumAmount);
                }
                peopleVector.remove(personNode2);
            }
            String resultString = "POSSIBLE";
            for (Object tempFriendsObj : peopleVector) {
                PersonDto personNode = (PersonDto) tempFriendsObj;
                if (personNode.getPersonOwe() != 0) {
                    resultString = "IMPOSSIBLE";
                }
            }
            System.out.println("\nresult : " + resultString + "\n");
        }
        catch (Exception e){
            System.out.println("\nresult : UNKNOWN \n");
            e.printStackTrace();
        }
    }



    public static void readFromMemory() {

        friendsCount = 5;
        friendshipsCount = 3;

        peopleVector = new Vector();

        PersonDto  personDto = new PersonDto();
        personDto.setPersonName("0");
        personDto.setPersonOwe(100);
        peopleVector.add(personDto);

        personDto = new PersonDto();
        personDto.setPersonName("1");
        personDto.setPersonOwe(-75);
        peopleVector.add(personDto);

        personDto = new PersonDto();
        personDto.setPersonName("2");
        personDto.setPersonOwe(-25);
        peopleVector.add(personDto);

        personDto = new PersonDto();
        personDto.setPersonName("3");
        personDto.setPersonOwe(-42);
        peopleVector.add(personDto);

        personDto = new PersonDto();
        personDto.setPersonName("4");
        personDto.setPersonOwe(42);
        peopleVector.add(personDto);

        friendshipList = new int[friendshipsCount][2];
        friendshipList[0][0] = 0;
        friendshipList[0][1] = 1;

        friendshipList[1][0] = 1;
        friendshipList[1][1] = 2;

        friendshipList[2][0] = 3;
        friendshipList[2][1] = 4;

    }

    public static void readDataFromScanner() {

        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter your friends and friendships count :\n");

        int friendsCount = reader.nextInt();
        int friendshipsCount = reader.nextInt();

        if(friendsCount < 2 || friendsCount > maxFriendsCount){
            System.out.println("friends count error.\n");
            return;
        }
        if(friendshipsCount < 0 || friendshipsCount > maxFriendshipCount){
            System.out.println("friendships count error.\n");
            return;
        }

        peopleVector = new Vector();
        int sumAmount = 0;
        for(int i = 0; i < friendsCount; i++){
            System.out.println("Please enter friend " + (i+1) + " owes or owed amount : ");
            PersonDto  personDto = new PersonDto();
            personDto.setPersonName((String.valueOf(i + 1)));
            personDto.setPersonOwe(reader.nextInt());
            peopleVector.add(personDto);
            sumAmount += personDto.getPersonOwe();
        }
        if(sumAmount != 0){
            System.out.println("Sum amounts which friend owe or owed are not zero, so it is IMPOSSIBLE ");
            return;
        }

        friendshipList = new int[friendshipsCount][2];

        for(int i = 0; i < friendshipsCount; i++){

            System.out.println("Please enter friendships " + (i+1) + " : \n");
            friendshipList[i][0] = reader.nextInt();
            friendshipList[i][1] = reader.nextInt();
        }
    }

    /*
     *  Reads data from files
     */
    static boolean readDataFromFile() {

        FileReader fileReader = null;
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fileName)));
            lnr.skip(Long.MAX_VALUE);
            int lineNumbers = lnr.getLineNumber() + 1;

            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineContent;
            fileData = new String[lineNumbers];
            for ( int i = 0; i < lineNumbers; i++) {
                lineContent = bufferedReader.readLine();
                if(lineContent == null)
                    break;
                fileData[i] = lineContent;
            }
            String lineData  = fileData[0];
            int spaceIndex   = lineData.indexOf(" ");
            friendsCount     = Integer.parseInt(lineData.substring(0,spaceIndex).trim());
            friendshipsCount = Integer.parseInt(lineData.substring(spaceIndex, lineData.length()).trim());

            if(friendsCount > maxFriendsCount ){
                System.out.println("Invalid friends count!");
                return false;
            }

            if(friendshipsCount > maxFriendshipCount ){
                System.out.println("Invalid friendships count!");
                return false;
            }

            if(lineNumbers - 1 != friendsCount + friendshipsCount){
                System.out.println("Invalid file Lines!");
                return false;
            }
            peopleVector = new Vector();
            PersonDto personDto;
            int currentLine = 0;
            for(int i =0; i < friendsCount; i++) {
                personDto = new PersonDto();
                personDto.setPersonName(String.valueOf(i));
                personDto.setPersonOwe(Long.parseLong(fileData[i + 1].trim()));
                peopleVector.add(personDto);
                currentLine++;
            }
            friendshipList = new int[friendshipsCount][2];
            for(int i =0; i < friendshipsCount; i++) {
                lineData  = fileData[currentLine + i + 1];
                spaceIndex   = lineData.indexOf(" ");
                friendshipList[i][0] = Integer.parseInt(lineData.substring(0,spaceIndex).trim());
                friendshipList[i][1] = Integer.parseInt(lineData.substring(spaceIndex, lineData.length()).trim());

            }

        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (fileReader != null)
                    fileReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    public static void main(String[] args) {

//        readFromMemory();
//        readDataFromScanner();
        if(!readDataFromFile()) {
            System.out.print("error reading file!");
            return;
        }
        MoneyMatters moneyMatters = new MoneyMatters(friendsCount,friendshipsCount,peopleVector,friendshipList);
        moneyMatters.exploreMoneyProblem();
    }
}

class PersonDto {

    private String personName;
    private long    personOwe;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public long getPersonOwe() {
        return personOwe;
    }

    public void setPersonOwe(long personOwe) {
        this.personOwe = personOwe;
    }
}