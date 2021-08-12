package sample;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class ContactBook {
    public ArrayList<Contact> contacts;
    public FileWriter file;

    public ContactBook() {
        this.contacts = new ArrayList<Contact>();
    }

    public void addContact(String fn, String ln, String num, String email) throws IOException, ParseException {
        File f = new File("./src/sample/Contacts.json");
        if (!f.exists()) {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            obj.put("FirstName", fn);
            obj.put("LastName", ln);
            obj.put("PhoneNumber", num);
            obj.put("Email", email);
            arr.add(obj);
            JSONObject fobj = new JSONObject();
            fobj.put("Contacts", arr);

            try {
                file = new FileWriter("./src/sample/Contacts.json");
                file.write(fobj.toString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            JSONParser parser = new JSONParser();
            Object objec = parser.parse(new FileReader("./src/sample/Contacts.json"));

            JSONObject jsonObject = (JSONObject) objec;
            JSONArray jsonArray = (JSONArray) jsonObject.get("Contacts");
            JSONObject contactsObj = new JSONObject();
            contactsObj.put("FirstName", fn);
            contactsObj.put("LastName", ln);
            contactsObj.put("PhoneNumber", num);
            contactsObj.put("Email", email);
            jsonArray.add(contactsObj);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("Contacts", jsonArray);

            try {
                file = new FileWriter("./src/sample/Contacts.json");
                file.write(jsonObject1.toString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readJson(String filename){
        JSONParser parser = new JSONParser();
        try{
            this.contacts.clear();
            FileReader reader = new FileReader(filename);
            Object obj = parser.parse(reader);
            JSONObject file = (JSONObject) obj;
            JSONArray contacts = (JSONArray) file.get("Contacts");
            for (int i = 0; i < contacts.size(); i++) {
                JSONObject cont = (JSONObject) contacts.get(i);
                String firstName = (String) cont.get("FirstName");
                String lastName = (String) cont.get("LastName");
                String number = (String) cont.get("PhoneNumber");
                String email = (String) cont.get("Email");

                this.contacts.add(new Contact(firstName, lastName, number, email));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public void updateJson(Contact c, Contact updatedContact)
    {
        JSONParser parser = new JSONParser();
        try{
            Object objec = parser.parse(new FileReader("./src/sample/Contacts.json"));
            JSONObject file = (JSONObject) objec;
            JSONArray contacts = (JSONArray) file.get("Contacts");
            for (int i = 0; i < contacts.size(); i++) {
                JSONObject cont = (JSONObject) contacts.get(i);
                String firstName = (String) cont.get("FirstName");
                String lastName = (String) cont.get("LastName");
                String number = (String) cont.get("PhoneNumber");
                String email = (String) cont.get("Email");

                Contact contactFound = new Contact(firstName, lastName, number, email);

                if (contactFound.equals(c))
                {

                    cont.put("FirstName", updatedContact.getFirstName());
                    cont.put("LastName", updatedContact.getLastName());
                    cont.put("PhoneNumber", updatedContact.getPhoneNumber());
                    cont.put("Email", updatedContact.getEmail());
                    FileWriter fileW = new FileWriter("./src/sample/Contacts.json");
                    fileW.write(file.toString());
                    fileW.flush();
                    fileW.close();
                    int index = this.contacts.indexOf(c);
                    this.contacts.set(index, updatedContact);
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void deleteJson(Contact c)
    {
        JSONParser parser = new JSONParser();
        try{
            Object objec = parser.parse(new FileReader("./src/sample/Contacts.json"));
            JSONObject file = (JSONObject) objec;
            JSONArray contacts = (JSONArray) file.get("Contacts");
            for (int i = 0; i < contacts.size(); i++) {
                JSONObject cont = (JSONObject) contacts.get(i);
                String firstName = (String) cont.get("FirstName");
                String lastName = (String) cont.get("LastName");
                String number = (String) cont.get("PhoneNumber");
                String email = (String) cont.get("Email");

                Contact contactFound = new Contact(firstName, lastName, number, email);

                if (contactFound.equals(c))
                {
                    contacts.remove(cont);
                    FileWriter fileW = new FileWriter("./src/sample/Contacts.json");
                    fileW.write(file.toString());
                    fileW.flush();
                    fileW.close();
                    int index = this.contacts.indexOf(c);
                    this.contacts.remove(index);
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Contact> findName(String name){
        ArrayList<Contact> contactList = new ArrayList<>();
        String first = "";
        String last = "";
        for (Contact c: this.contacts)
        {
            first = c.getFirstName().toLowerCase();
            last = c.getLastName().toLowerCase();
            if(first.contains(name.toLowerCase()) || last.contains(name.toLowerCase())){
                contactList.add(c);
            }
        }
        return contactList;
    }

}
