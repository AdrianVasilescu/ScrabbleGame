package main.Common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileInputReader {
    public static List<Tile> getInput(int n)
    {
        List<Tile> tiles = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(".\\resources\\input" + n + ".txt"));
            String line = reader.readLine();
            while(line != null)
            {
                tiles.add(parseTile(line));
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tiles;
    }

    private static Tile parseTile(String line) {
        String[] input = line.split("-");
        Tile t = new Tile(input[2].toCharArray()[0], 1);
        t.setRow(Integer.parseInt(input[0]+"")-1);
        t.setColumn(Integer.parseInt(input[1]+"")-1);

        return t;
    }
}
