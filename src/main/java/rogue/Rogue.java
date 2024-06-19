package rogue;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//import java.io.IOException;

import java.awt.Point;



/**
* Rogue class to create a game.
*@author Alireza Sharif
*/
public class Rogue {

    private Player player;
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private RogueParser parser;
    private HashMap<String, Character> symbols = new HashMap<String, Character>();
    private static final int MAXDOORS = 4;
    public static final char UP = 'h';
    public static final char DOWN = 'l';
    public static final char LEFT = 'j';
    public static final char RIGHT = 'k';
    public static final char CHANGEROOM = 'r';  // move player the room with higher id ,if not start from the first room
    private String nextDisplay = "";
    /**
    * default constructor.
    */
    public Rogue() {

    }
    /**
    * class constructor to get items and rooms from parser.
    *@param theDungeonInfo
    */
    public Rogue(RogueParser theDungeonInfo) {

        parser = theDungeonInfo;
        setSymbols();
        Map roomInfo = parser.nextRoom();
        while (roomInfo != null) {
            addRoom(roomInfo);
            roomInfo = parser.nextRoom();
        }

        Map itemInfo = parser.nextItem();
        while (itemInfo != null) {
            addItem(itemInfo);
            itemInfo = parser.nextItem();
        }
        // connect doors
        setConnectedDoors();
        // verify all the rooms
        verifyRooms();
    }

    /**
    *add a room to the rooms list.
    *@param toAdd
    */
    public void addRoom(Map<String, String> toAdd) {

        Room newRoom = new Room();
        int width = Integer.decode(toAdd.get("width"));
        newRoom.setWidth(width);
        int height = Integer.decode(toAdd.get("height"));
        newRoom.setHeight(height);
        int id = Integer.decode(toAdd.get("id"));
        newRoom.setId(id);
        Boolean playerInRoom = Boolean.parseBoolean(toAdd.get("start"));
        newRoom.playerInRoom(playerInRoom);

        ArrayList<String> directions = new ArrayList<String>();
        directions.add("N");
        directions.add("S");
        directions.add("E");
        directions.add("W");

        for (String dir : directions) {

             if (Integer.decode(toAdd.get(dir)) != -1) {
                Door newDoor = new Door();
                newDoor.setDir(dir);
                newDoor.setPos(Integer.decode(toAdd.get(dir)));
                newDoor.setConDoor(Integer.decode(toAdd.get(dir + "conRoomId")));
                newDoor.connectRoom(newRoom);
                newRoom.setDoor(newDoor);
            }

        }


        if (playerInRoom) {
            newRoom.setPlayer(player);
            //player.setCurrentRoom(newRoom);
        }
        newRoom.setRogue(this);
        rooms.add(newRoom);
    }

    /**
    * Add an item to items list.
    *@param toAdd
    */
    public void addItem(Map<String, String> toAdd) {

        Item newItem = new Item();
        newItem.setId(Integer.decode(toAdd.get("id")));
        newItem.setName(toAdd.get("name"));
        newItem.setType(toAdd.get("type"));
        newItem.setDescription(toAdd.get("description"));
        newItem.setDisplayCharacter(symbols.get(newItem.getType().toUpperCase()));
        if (toAdd.containsKey("room")) {
            int x = Integer.decode(toAdd.get("x"));
            int y = Integer.decode(toAdd.get("y"));
            Point location = new Point(x, y);
            newItem.setXyLocation(location);
            items.add(newItem);
            for (Room room : rooms) {

                if (room.getId() == Integer.decode(toAdd.get("room"))) {

                    newItem.setCurrentRoom(room);
                    Boolean added = false;
                    while (!added) {
                        try {

                            room.addItem(newItem);
                            added = true;
                            break;
                        } catch (ImpossiblePositionException e) {
                            // generate a random position
                            int min = 1;
                            int maxX = room.getWidth();
                            int maxY = room.getHeight();
                            int randX = (int) (Math.random() * (maxX - min + 1) + min);
                            int randY = (int) (Math.random() * (maxY - min + 1) + min);
                            location.setLocation(randX, randY);
                            newItem.setXyLocation(location);
                        } catch (NoSuchItemException er) {
                            added = true;
                        }
                    }

                    break;
                }
            }
        }

       // items.add(newItem);

    }

    /**
    * verify all the rooms.
    */
    public void verifyRooms() {
        for (Room room : rooms) {
            try {
                room.verifyRoom();
            } catch (NotEnoughDoorsException e) {
                Boolean added = false;
                for (Room otherRoom : rooms) {
                    // if another room with less than four doors found , add a new N door to room
                    if (otherRoom.getId() != room.getId() && otherRoom.getDoors().size() < MAXDOORS) {
                        Door newDoor = new Door();
                        newDoor.setDir("N");
                        newDoor.setPos(2);
                        newDoor.setConDoor(otherRoom.getId());
                        newDoor.connectRoom(room);
                        newDoor.connectRoom(otherRoom);
                        room.setDoor(newDoor);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    System.out.println("This dungeon file cannot be used");
                    System.exit(0);
                }
            }
        }
    }
    /**
    * check if an item is on the items list.
    *@param theItem
    *@return (Boolean)
    */
    public Boolean containsItem(Item theItem) {
        for (Item item : items) {
            if (theItem.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }
    /**
    * method to add the connecting room to each door in all the rooms.
    */
    public void setConnectedDoors() {

        for (Room room : rooms) {
            ArrayList<Door> roomDoors = new ArrayList<Door>();
            roomDoors = room.getDoors();
            for (Door door : roomDoors) {
                for (Room conRoom : rooms) {
                    if (door.getConDoor() == conRoom.getId()) {
                        door.connectRoom(conRoom);
                        break;
                    }
                }
            }
        }
    }

    /**
    * set player in the game.
    *@param thePlayer
    */
    public void setPlayer(Player thePlayer) {
        player = thePlayer;

        for (Room room : rooms) {
            if (room.isPlayerInRoom()) {
                room.setPlayer(player);
                room.setPlayerLocation();
                player.setCurrentRoom(room);
            }
        }
    }

    /**
    * get the symbols from the parser.
    *
    */
    public void setSymbols() {

        symbols = parser.getSymbols();

    }
    /**
    * get all the rooms.
    *@return rooms
    */
    public ArrayList<Room> getRooms() {

        return rooms;
    }
    /**
    * get all the items.
    *@return items
    */
    public ArrayList<Item> getItems() {
        return items;
    }
    /**
    * get the player.
    *@return player
    */
    public Player getPlayer() {
        return player;
    }

    /**
    * create an string to print all the rooms in the game.
    * @return allRooms
    */

    public String displayAll() {
        //creates a string that displays all the rooms in the dungeon
        int i = 1;
        String roomPrint = "";
        String allRooms = "";
        for (Room myRoom : rooms) {

            allRooms = allRooms + "<-----[Room" + i + " ]----->\n";
            if (i == 1) {
                allRooms = allRooms + "- Starting Room\n";
            }

            roomPrint = putCharsInRoom(myRoom.displayRoom());
            allRooms = allRooms + roomPrint + "\n\n";
            i++;

        }
        return allRooms;
    }

    /**
    * put symbols characters in the room display string.
    * @param roomPrint
    * @return (String)
    */
    public String putCharsInRoom(String roomPrint) {

            String editedRoom1 = "";
            String editedRoom2 = "";
            String editedRoom3 = "";
            String editedRoom4 = "";
            String editedRoom5 = "";
            String editedRoom6 = "";
            String editedRoom7 = "";
            String editedRoom8 = "";
            String editedRoom9 = "";
            String editedRoom10 = "";
            String finalRoom = "";

            editedRoom1 = roomPrint.replaceAll("FLOOR", Character.toString(symbols.get("FLOOR")));
            editedRoom2 = editedRoom1.replaceAll("DOOR", Character.toString(symbols.get("DOOR")));
            editedRoom3 = editedRoom2.replaceAll("GOLD", Character.toString(symbols.get("GOLD")));
            editedRoom4 =  editedRoom3.replaceAll("PASSAGE", Character.toString(symbols.get("PASSAGE")));
            editedRoom5 = editedRoom4.replaceAll("PLAYER", Character.toString(symbols.get("PLAYER")));
            editedRoom6 = editedRoom5.replaceAll("NS_WALL", Character.toString(symbols.get("NS_WALL")));
            editedRoom7 = editedRoom6.replaceAll("FOOD", Character.toString(symbols.get("FOOD")));
            editedRoom8 = editedRoom7.replaceAll("ARMOR", Character.toString(symbols.get("ARMOR")));
            editedRoom9 = editedRoom8.replaceAll("SCROLL", Character.toString(symbols.get("SCROLL")));
            editedRoom10 = editedRoom9.replaceAll("POTION", Character.toString(symbols.get("POTION")));
            finalRoom = editedRoom10.replaceAll("EW_WALL", Character.toString(symbols.get("EW_WALL")));

            return finalRoom;
    }

        /** this method assesses a move to ensure it is valid.
        * If the move is valid, then the display resulting from the move
        * is calculated and set as the 'nextDisplay' (probably a private member variable)
        * If the move is not valid, an InvalidMoveException is thrown
        * and the nextDisplay is unchanged
        * @param input
        * @return (String)
        *@throws InvalidMoveException
        */
    public String makeMove(char input) throws InvalidMoveException {

        // get player location
        Point playerLoc = player.getXyLocation();
        int x = (int) playerLoc.getX();
        int y = (int) playerLoc.getY();
        try {
            if (input == UP) {
                navigate(0, -1);

            } else if (input == DOWN) {
                navigate(0, 1);

            } else if (input == RIGHT) {
                navigate(1, 0);

            } else if (input == LEFT) {
                navigate(-1, 0);
            } else if (input == CHANGEROOM) {
                    changePlayerRoom();
            } else {
                throw new InvalidMoveException();
            }
        } catch (InvalidMoveException e) {
            return charToCommand(input);
        }
        return charToCommand(input);

    }
    // convert input character to a string
    private String charToCommand(Character c) {

        if (c == DOWN) {
            return "Nice Move, You are going Down!";
        } else if (c == UP) {
            return "Nice Move, You are going Up!";
        } else if (c == RIGHT) {
            return "Nice Move, You are going Right!";
        } else if (c == LEFT) {
            return "Nice Move, You are going Left!";
        } else if (c == CHANGEROOM) {
            return "Nice Move, You changed your room!";
        } else {
            return " Sorry Not a valid command , \n please try again! see README for instructions!";
        }
    }
    // change players location to the room with higher id,
    // if no room with higher id , start from room 1.
    // put the player at (2,2)
    private void changePlayerRoom() {

        Room currRoom = player.getCurrentRoom();
        // check if there is a room with higher id
        Boolean changed = false;
        for (Room room : rooms) {
            if (room.getId() == currRoom.getId() + 1) {
                currRoom.playerInRoom(false);
                player.setCurrentRoom(room);
                player.getCurrentRoom().playerInRoom(true);
                Point newLoc = new Point(2, 2);
                player.setXyLocation(newLoc);
                player.getCurrentRoom().setPlayer(player);
                changed = true;
                break;
            }
        }
        // if no higher room put player on first room
        if (!changed) {
            for (Room room : rooms) {
                if (room.getId() == 1) {
                    currRoom.playerInRoom(false);
                    player.setCurrentRoom(room);
                    player.getCurrentRoom().playerInRoom(true);
                    Point newLoc = new Point(2, 2);
                    player.setXyLocation(newLoc);
                    player.getCurrentRoom().setPlayer(player);
                    changed = true;
                    break;
                }
            }
        }
        setCurrentRoomDisplay();

    }
    // get the x,y and add them to players location to decide what happens on next move
    private void navigate(int addX, int addY) throws InvalidMoveException {
        // get player location
        Point playerLoc = player.getXyLocation();
        int x = (int) playerLoc.getX();
        int y = (int) playerLoc.getY();
        x = x + addX;
        y = y + addY;
        int width = player.getCurrentRoom().getWidth();
        int height = player.getCurrentRoom().getHeight();
        // if player on the walls
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            // check if there is a door
            Boolean isDoor = false;
            Room currRoom = player.getCurrentRoom();
            isDoor = checkPointOnDoorNS(currRoom, x, y, playerLoc, width, height)
            | checkPointOnDoorEW(currRoom, x, y, playerLoc, width, height);
            if (isDoor) {
               setCurrentRoomDisplay();
            } else {
                throw new InvalidMoveException("You can't move in to walls!");
            }
        } else {
            // move in the room & disappear the item
            Point newPlayerLoc = new Point(x, y);
            for (Item item : player.getCurrentRoom().getRoomItems()) {
                if (newPlayerLoc.equals(item.getXyLocation())) {
                    player.getCurrentRoom().getRoomItems().remove(item);
                    break;
                }
            }
            // set new player location
           player.setXyLocation(newPlayerLoc);
           setCurrentRoomDisplay();
        }
    }

    // check if new players position matches any of N or S doors.
    private Boolean checkPointOnDoorNS(Room currRoom, int x, int y, Point playerLoc, int width, int height) {

        Boolean isDoor = false;
        for (Door door : currRoom.getDoors()) {
            if (door.getDir().equals("N")) {
                if (y == 0 && x == door.getPos()) {
                    isDoor = true;
                    currRoom.playerInRoom(false);
                    player.setCurrentRoom(door.getOtherRoom(currRoom));
                    player.getCurrentRoom().playerInRoom(true);
                    for (Door otherDoor : door.getOtherRoom(currRoom).getDoors()) {
                        if (otherDoor.getDir().equals("S")) {
                            playerLoc.setLocation(otherDoor.getPos(),
                            player.getCurrentRoom().getHeight() - 1);
                            player.setXyLocation(playerLoc);
                            player.getCurrentRoom().setPlayer(player);
                            break;
                        }
                    }
                    break;
                }
                    } else if (door.getDir().equals("S")) {
                        if (y == height - 1 && x == door.getPos()) {
                            isDoor = true;
                            currRoom.playerInRoom(false);
                            player.setCurrentRoom(door.getOtherRoom(currRoom));
                            player.getCurrentRoom().playerInRoom(true);
                            for (Door otherDoor : door.getOtherRoom(currRoom).getDoors()) {
                                if (otherDoor.getDir().equals("N")) {
                                    playerLoc.setLocation(otherDoor.getPos(), 0);
                                    player.setXyLocation(playerLoc);
                                    player.getCurrentRoom().setPlayer(player);
                                    break;
                                }
                            }
                            break;
                        }
                    }
            }
            return isDoor;
    }
    // check if new players position matches any of W or E doors.
    private Boolean checkPointOnDoorEW(Room currRoom, int x, int y, Point playerLoc, int width, int height) {
        Boolean isDoor = false;
        for (Door door : currRoom.getDoors()) {
            if (door.getDir().equals("W")) {
                if (x == 0 && y == door.getPos()) {
                    isDoor = true;
                    currRoom.playerInRoom(false);
                    player.setCurrentRoom(door.getOtherRoom(currRoom));
                    player.getCurrentRoom().playerInRoom(true);
                    for (Door otherDoor : door.getOtherRoom(currRoom).getDoors()) {
                        if (otherDoor.getDir().equals("E")) {
                            playerLoc.setLocation(player.getCurrentRoom().getWidth() - 1,
                            otherDoor.getPos());
                            player.setXyLocation(playerLoc);
                            player.getCurrentRoom().setPlayer(player);
                            break;
                        }
                    }
                    break;
                }
            } else if (door.getDir().equals("E")) {
                if (x == width - 1 && y == door.getPos()) {
                    isDoor = true;
                    currRoom.playerInRoom(false);
                    player.setCurrentRoom(door.getOtherRoom(currRoom));
                    player.getCurrentRoom().playerInRoom(true);
                    for (Door otherDoor : door.getOtherRoom(currRoom).getDoors()) {
                        if (otherDoor.getDir().equals("W")) {
                            playerLoc.setLocation(0, otherDoor.getPos());
                            player.setXyLocation(playerLoc);
                            player.getCurrentRoom().setPlayer(player);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return isDoor;
    }

    /**
    *get the next string to draw on screen.
    *@return (String)
    */
    public String getNextDisplay() {

         return nextDisplay;
    }

    /**
    * get the starting room to display.
    */
    public void setCurrentRoomDisplay() {
        String currRoom = "";
        for (Room room : rooms) {
            if (room.isPlayerInRoom()) {
                currRoom = room.displayRoom();
                break;
            }
        }
        nextDisplay = putCharsInRoom(currRoom);
    }
}
