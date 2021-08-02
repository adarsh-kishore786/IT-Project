# Test

Use this branch for testing

``` char choiceMain;
        Scanner charIn = new Scanner(System.in);
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("                 WELCOME TO VIVLIO LIBRARY!                   ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        do
        {
            System.out.println("\n\nEnter your option (1/2/3) according to the menu: ");
            System.out.println("1. Customer");
            System.out.println("2. Administration");
            System.out.println("3. Exit");
            int check=charIn.nextInt();
            switch(check)
            {
                case 1:
                callCustomer();
                break;
                case 2:
                callAdmin();
                break;
                case 3:
                System.out.println("Exiting the program!");
                System.exit(0);
                break;
                default:
                System.out.println("Wrong choice entered!");
            }
            System.out.println("Do you want to continue in Main? Enter Y if yes else press any key: ");
            choiceMain=charIn.next().charAt(0);
        } while (choiceMain=='Y');
        charIn.close(); ```

        ```
        public static void callAdmin() {
        Scanner inputManage = new Scanner(System.in);
        String password;
        char choiceAdmin;
        do
        {
            System.out.println("Welcome to the Administration section");
            System.out.println("Please enter the password to continue: ");
            password=inputManage.nextLine();
            if (password.compareTo("supersecretpassword")==0) //Better implementation of this part xD
            {
                //Admin related things
                System.out.println("\n\nEnter your option (1/2/3) according to the menu: ");
                System.out.println("1. Manage books");
                System.out.println("2. ");
                System.out.println("3. Go back to main");
                int check=inputManage.nextInt();
                switch(check)
                {
                    case 1:
                    //Manage books function
                    break;
                    case 2:
                    //Whatever functionalities available in Admin, expand the menu
                    break;
                    case 3:
                    System.out.println("Going back to main");
                    //Main.main();
                    break;
                    default:
                    System.out.println("Wrong choice entered!");
                }
            }
            else
            System.out.println("Wrong password!");
            System.out.println("Do you want to continue in Admin? Enter Y if yes else press any key: ");
            choiceAdmin=inputManage.next().charAt(0);
        } while (choiceAdmin=='Y');
        
        inputManage.close();

    }
    public static void callCustomer() {

    }
    ```

