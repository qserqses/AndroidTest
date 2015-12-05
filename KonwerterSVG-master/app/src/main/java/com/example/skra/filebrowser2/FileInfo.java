package com.example.skra.filebrowser2;

/**
 * Klasa używana do przechowywania tylko potrzebnych do obsługi przeglądarki informacji o plikach
 * W przypadku dużej ilości plików w katalogu lista złożona z obiektów typu File mogła by zajmować dużo miejsca
 */
public class FileInfo {
    private String name, path;
    private boolean isDirectory;

    FileInfo(String name, String path, boolean isDirectory)
    {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return path;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }
}
