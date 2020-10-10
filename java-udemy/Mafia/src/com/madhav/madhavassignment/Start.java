package com.madhav.madhavassignment;

import java.util.ArrayList;
import java.util.Scanner;

public class Start {
    private int N;
    private int nc,nm,nd,nh,rp;
    private ArrayList<Game> players=new ArrayList<>();
    private ArrayList<Integer> mafia=new ArrayList<>();
    private ArrayList<Integer> commoner=new ArrayList<>();
    private ArrayList<Integer> detective=new ArrayList<>();
    private ArrayList<Integer> healer=new ArrayList<>();
    private boolean[] alive;
    private static Scanner scan=new Scanner(System.in);
    private int user;
    private void generate_player(int np,boolean is_mafia,boolean is_detective,boolean is_healer,boolean is_commoner)
    {
        int rCount=np;
        while (rCount>0)
        {
            int c=(int)(Math.random()*this.N);
            if(players.get(c)==null)
            {
                if(is_mafia)
                {
                    mafia.add(c);
                    players.set(c,new Mafia());
                    rCount--;
                }
                else if(is_detective)
                {
                    detective.add(c);
                    players.set(c,new Detective());
                    rCount--;
                }
                else if(is_healer)
                {
                    healer.add(c);
                    players.set(c,new Healer());
                    rCount--;
                }
                else if(is_commoner)
                {
                    commoner.add(c);
                    players.set(c,new Commoner());
                    rCount--;
                }
            }
        }
    }
    Start(int N)
    {
        this.N=N;
        nm=this.N/5;
        nd=this.N/5;
        nh=Math.max(1,this.N/10);
        nc=this.N-nm-nd-nh;
        rp=this.N;
        for(int i=0;i<this.N;i++)
        {
            players.add(null);
        }
        generate_player(nm,true,false,false,false);
        generate_player(nd,false,true,false,false);
        generate_player(nh,false,false,true,false);
        generate_player(nc,false,false,false,true);
        alive=new boolean[this.N];
        for (int i=0;i<this.N;i++)
        {
            alive[i]=true;
        }
    }
    public void play()
    {
        System.out.println("Choose a Character");
        System.out.println("1) Mafia\n2) Detective\n3) Healer\n4) Commoner\n5) Assign Randomly");
        int val=scan.nextInt();
        scan.nextLine();
        boolean play=true;
        while (play)
        {
            switch (val)
            {
                case 1:play_mafia();play=false;break;
                case 2:play_detective();play=false;break;
                case 3:play_healer();play=false;break;
                case 4:play_commoner();play=false;break;
                case 5:val=(int)(Math.random()*5+1);break;
                default: System.out.println("Choose correct value");
            }
        }

    }

    private void print_players()
    {
        for(int m:mafia)
        {
            if(m==user)
            {
                System.out.print(" Player"+(m+1)+"[user] ");
                continue;
            }

            System.out.print(" [Player"+(m+1)+"] ");
        }
        System.out.print(" were mafia\n");
        for(int d:detective)
        {
            if(d==user)
            {
                System.out.print(" Player"+(d+1)+"[user] ");
                continue;
            }
            System.out.print(" [Player"+(d+1)+"] ");
        }
        System.out.print(" were detective\n");
        for(int h:healer)
        {
            if(h==user)
            {
                System.out.print(" Player"+(h+1)+"[user] ");
                continue;
            }
            System.out.print(" [Player"+(h+1)+"] ");
        }
        System.out.print(" were healer\n");
        for(int c:commoner)
        {
            if(c==user)
            {
                System.out.print(" Player"+(c+1)+"[user] ");
                continue;
            }
            System.out.print(" [Player"+(c+1)+"] ");
        }
        System.out.print(" were commoner\n");
    }
    private void play_mafia()
    {
        boolean play=true;
        int user=mafia.get(0);
        this.user=user;
        System.out.println("You are Player"+(user+1));
        System.out.print("You are a mafia.Other mafia are:");
        for(int m:mafia)
        {
            if(m!=user)
            {
                System.out.print(" [Player"+(m+1)+"] ");
            }
        }
        System.out.println();
        while (play)
        {
            if(nm==0)
            {
                play=false;
                System.out.println("The Mafias have lost.");
                print_players();
                break;
            }
            else if(nm==(nc+nd+nh))
            {
                play=false;
                System.out.println("The Mafias have won.");
                print_players();
                break;
            }
            System.out.print(rp+" players are remaining: ");
            for(int i=0;i<this.N;i++)
            {
                if(this.alive[i])
                {
                    System.out.print(" Player"+(i+1));
                }
            }
            System.out.println();
            int target_player=-1;
            if(alive[user])
            {
                System.out.println("Choose a target:");
                target_player=scan.nextInt()-1;
                scan.nextLine();
                while (!alive[target_player]||mafia.contains(target_player))
                {
                    if(!alive[target_player])
                    {
                        System.out.println("Player"+(target_player+1)+" is dead.Choose a player to target.");
                    }
                    else {
                        System.out.println("You cannot target a mafia.Choose a player to target:");
                    }
                    target_player=scan.nextInt()-1;
                    scan.nextLine();
                }
            }
            else
            {
                target_player=mafia_target();
                System.out.println("Mafias have chosen their target");
            }
            handle_mafia_target(target_player);
            boolean chosen_mafia=false;
            int chosen_player=-1;
            if(nd==0)
            {
                System.out.println("Detectives have chosen a player to test.");
            }
            else
            {
                chosen_player=detective_target();
                chosen_mafia=handle_detective_target(chosen_player);
                System.out.println("Detectives have chosen a player to test.");
            }
            if(nh==0)
            {
                System.out.println("Healers have chosen some one to heal");
            }
            else
            {
                int chosen_healer_player=healer_target();
                players.get(chosen_healer_player).heal();
                System.out.println("Healers have chosen some one to heal");

            }
            Game target=players.get(target_player);
            if(target.getHp()==0)
            {
                System.out.println("Player"+(target_player+1)+" has died.");
                alive[target_player]=false;
                manage_no_alive(target);
            }
            else
            {
                System.out.println("No one died.");
            }
            int chosen_vote_out=-1;
            if(chosen_mafia)
            {
                chosen_vote_out=chosen_player;
            }
            else
            {
                if(alive[user])
                {
                    System.out.println("Select a player to vote out:");
                    int user_vote_out=scan.nextInt();
                    scan.nextLine();
                }
                chosen_vote_out=vote_out();
            }

            Game chosen_vote_out_player=players.get(chosen_vote_out);
            chosen_vote_out_player.dead();
            alive[chosen_vote_out]=false;
            manage_no_alive(chosen_vote_out_player);
            System.out.println("Player"+(chosen_vote_out+1)+" has been voted out");

        }
    }
    private void play_detective()
    {
        boolean play=true;
        int user=detective.get(0);
        this.user=user;
        System.out.println("You are Player"+(user+1));
        System.out.print("You are a detective.Other detectives are:");
        for(int w:detective)
        {
            if(w!=user)
            {
                System.out.print("[Player"+(w+1)+"] ");
            }
        }
        System.out.println();
        while (play)
        {
            if(nm==0)
            {
                play=false;
                System.out.println("The Mafias have lost.");
                print_players();
                break;
            }
            else if(nm==(nc+nd+nh))
            {
                play=false;
                System.out.println("The Mafias have won.");
                print_players();
                break;
            }
            System.out.print(rp+" players are remaining: ");
            for(int i=0;i<this.N;i++)
            {
                if(this.alive[i])
                {
                    System.out.print(" Player"+(i+1));
                }
            }
            System.out.println();
            int target_player=mafia_target();
            System.out.println("Mafias have chosen their target");
            handle_mafia_target(target_player);
            boolean chosen_mafia=false;
            int chosen_player=-1;
            if(nd==0)
            {
                 System.out.println("Detectives have chosen a player to test.");
            }
            else if(alive[user])
            {
                System.out.println("Choose a player to test ");
                chosen_player=scan.nextInt()-1;
                scan.nextLine();
                while (!alive[chosen_player]||detective.contains(chosen_player))
                {
                    if(!alive[chosen_player])
                    {
                        System.out.println("Player"+(chosen_player+1)+" is dead.Choose a player to test");
                    }
                    else
                    {
                        System.out.println("You cannot test a detective.Choose a player to test:");
                    }
                    chosen_player=scan.nextInt()-1;
                    scan.nextLine();
                }
                if(mafia.contains(chosen_player)&&alive[chosen_player])
                {
                    System.out.println("Player"+(chosen_player+1)+" is a mafia.");
                    chosen_mafia=true;
                }
                else
                {
                    System.out.println("Player"+(chosen_player+1)+" is not a mafia.");
                }

            }
            else
            {
                chosen_player=detective_target();
                chosen_mafia=handle_detective_target(chosen_player);
                System.out.println("Detectives have chosen a player to test.");
            }
            if(nh==0)
            {
                System.out.println("Healers have chosen some one to heal");
            }
            else
            {
                int chosen_healer_player=healer_target();
                players.get(chosen_healer_player).heal();
                System.out.println("Healers have chosen some one to heal");
            }
            Game target=players.get(target_player);
            if(target.getHp()==0)
            {
                System.out.println("Player"+(target_player+1)+" has died.");
                alive[target_player]=false;
                manage_no_alive(target);
            }
            else
            {
                System.out.println("No one died.");
            }
            int chosen_vote_out=-1;
            if(chosen_mafia)
            {
                chosen_vote_out=chosen_player;
            }
            else
            {
                if (alive[user])
                {

                        System.out.println("Select a player to vote out:");
                        int user_vote_out=scan.nextInt();
                        scan.nextLine();

                }
                chosen_vote_out=vote_out();
            }
            Game chosen_vote_out_player=players.get(chosen_vote_out);
            chosen_vote_out_player.dead();
            alive[chosen_vote_out]=false;
            manage_no_alive(chosen_vote_out_player);
            System.out.println("Player"+(chosen_vote_out+1)+" has been voted out");

        }
    }
    private void play_healer()
    {
        boolean play=true;
        int user=healer.get(0);
        this.user=user;
        System.out.println("You are Player"+(user+1));
        System.out.print("You are a Healer.Other healers are:");
        for(int h:healer)
        {
            if(h!=user)
            {
                System.out.print("[Player"+(h+1)+"] ");
            }
        }
        System.out.println();
        while (play)
        {
            if(nm==0)
            {
                play=false;
                System.out.println("The Mafias have lost.");
                print_players();
                break;
            }
            else if(nm==(nc+nd+nh))
            {
                play=false;
                System.out.println("The Mafias have won.");
                print_players();
                break;
            }
            System.out.print(rp+" players are remaining: ");
            for(int i=0;i<this.N;i++)
            {
                if(this.alive[i])
                {
                    System.out.print(" Player"+(i+1));
                }
            }
            System.out.println();
            int target_player=mafia_target();
            System.out.println("Mafias have chosen their target");
            handle_mafia_target(target_player);
            boolean chosen_mafia=false;
            int chosen_player=-1;
            if(nd==0)
            {
                System.out.println("Detectives have chosen a player to test.");
            }
            else
            {
                chosen_player=detective_target();
                chosen_mafia=handle_detective_target(chosen_player);
                System.out.println("Detectives have chosen a player to test.");
            }
            if(nh==0)
            {
                System.out.println("Healers have chosen some one to heal");
            }
            else
            {
                int chosen_healer_player=-1;
                if(alive[user])
                {
                    System.out.println("Choose a player to heal:");
                    chosen_healer_player=scan.nextInt()-1;
                    scan.nextLine();
                    while (!alive[chosen_healer_player]||healer.contains(chosen_healer_player))
                    {
                        if(!alive[chosen_healer_player])
                        {
                            System.out.println("Player"+(chosen_healer_player+1)+"is dead.Choose a Player to heal");
                        }
                        System.out.println("You cannot choose another healer.Choose a player to heal:");
                        chosen_healer_player=scan.nextInt()-1;
                        scan.nextLine();
                    }
                }
               else {
                   chosen_healer_player=healer_target();
                    System.out.println("Healers have chosen some one to heal");
                }
                players.get(chosen_healer_player).heal();
            }
            Game target=players.get(target_player);
            if(target.getHp()==0)
            {
                System.out.println("Player"+(target_player+1)+" has died.");
                alive[target_player]=false;
                manage_no_alive(target);
            }
            else
            {
                System.out.println("No one died.");
            }
            int chosen_vote_out=-1;
            if(chosen_mafia)
            {
                chosen_vote_out=chosen_player;
            }
            else
            {
                if(alive[user])
                {
                    System.out.println("Select a player to vote out:");
                    int user_vote_out=scan.nextInt();
                    scan.nextLine();
                }
                chosen_vote_out=vote_out();
            }

            Game chosen_vote_out_player=players.get(chosen_vote_out);
            chosen_vote_out_player.dead();
            alive[chosen_vote_out]=false;
            manage_no_alive(chosen_vote_out_player);
            System.out.println("Player"+(chosen_vote_out+1)+" has been voted out");

        }
    }
    private void play_commoner()
    {
        boolean play=true;
        int user=commoner.get(0);
        this.user=user;
        System.out.println("You are Player"+(user+1));
        System.out.print("You are a Commoner.Other commoners are:");
        for(int c:commoner)
        {
            if(c!=user)
            {
                System.out.print("[Player"+(c+1)+"] ");
            }
        }
        System.out.println();
        while (play)
        {
            if(nm==0)
            {
                play=false;
                System.out.println("The Mafias have lost.");
                print_players();
                break;
            }
            else if(nm==(nc+nd+nh))
            {
                play=false;
                System.out.println("The Mafias have won.");
                print_players();
                break;
            }
            System.out.print(rp+" players are remaining: ");
            for(int i=0;i<this.N;i++)
            {
                if(this.alive[i])
                {
                    System.out.print(" Player"+(i+1));
                }
            }
            System.out.println();
            int target_player=mafia_target();
            System.out.println("Mafias have chosen their target");
            handle_mafia_target(target_player);
            boolean chosen_mafia=false;
            int chosen_player=-1;
            if(nd==0)
            {
                System.out.println("Detectives have chosen a player to test.");
            }
            else
            {
                chosen_player=detective_target();
                chosen_mafia=handle_detective_target(chosen_player);
                System.out.println("Detectives have chosen a player to test.");
            }
            if(nh==0)
            {
                System.out.println("Healers have chosen some one to heal");
            }
            else
            {
                int chosen_healer_player=healer_target();
                System.out.println("Healers have chosen some one to heal");
                players.get(chosen_healer_player).heal();
            }
            Game target=players.get(target_player);
            if(target.getHp()==0)
            {
                System.out.println("Player"+(target_player+1)+" has died.");
                alive[target_player]=false;
                manage_no_alive(target);
            }
            else
            {
                System.out.println("No one died.");
            }
            int chosen_vote_out=-1;
            if(chosen_mafia)
            {
                chosen_vote_out=chosen_player;
            }
            else
            {
                if(alive[user])
                {
                    System.out.println("Select a player to vote out:");
                    int user_vote_out=scan.nextInt();
                    scan.nextLine();
                }
                chosen_vote_out=vote_out();
            }

            Game chosen_vote_out_player=players.get(chosen_vote_out);
            chosen_vote_out_player.dead();
            alive[chosen_vote_out]=false;
            manage_no_alive(chosen_vote_out_player);
            System.out.println("Player"+(chosen_vote_out+1)+" has been voted out");

        }
    }
    private boolean handle_detective_target(int target_player)
    {
        if(mafia.contains(target_player)&&alive[target_player])
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void handle_mafia_target(int target_player)
    {
        Game target=players.get(target_player);
        int player_hp=target.getHp();
        int min_hp=Integer.MAX_VALUE,sum_hp=0,min_hp_index=-1;
        for(int w:mafia)
        {
            if(alive[w])
            {
                int val=players.get(w).getHp();
                sum_hp+=val;
                if(min_hp>val)
                {
                    min_hp=val;
                    min_hp_index=w;
                }
            }
        }
        if(sum_hp>player_hp)
        {
            ArrayList<Integer> min=new ArrayList<>();
//            int remaining_hp=player_hp/nm;
//            if(min_hp<player_hp) {
//                players.get(min_hp_index).dead();
//                player_hp -= min_hp;
//                min.add(min_hp_index);
//                remaining_hp = player_hp / (nm - 1);
//            }
//            else {
//                min_hp_index=-1;
//            }
            boolean all_val=false;
            while (!all_val)
            {
                int count=0;
                for(int w:mafia)
                {
                    if(alive[w]&&!min.contains(w)&&players.get(w).getHp()<player_hp)
                    {
                        player_hp-=players.get(w).getHp();
                        players.get(w).dead();
                        min.add(w);
                    }
                    else if(alive[w]){
                        count++;
                    }
                }
                if(count==nm)
                {
                    all_val=true;
                }
            }
            int remaining_hp=player_hp/(nm-min.size());
            for (int w : mafia) {
                    if (!min.contains(w) && alive[w]) {
                        Game x=players.get(w);
                        x.decreaseHp(remaining_hp);
                    }
                }
            target.dead();
        }
        else {
            target.decreaseHp(sum_hp);
            for(int w:mafia)
            {
                if(alive[w])
                {
                    players.get(w).dead();
                }
            }
        }
    }
    private void manage_no_alive(Game target)
    {
        String name=target.getName();
        if(name.equals("mafia"))
        {
            nm--;
            rp--;
        }
        else if(name.equals("detective"))
        {
            nd--;
            rp--;
        }
        else if(name.equals("healer"))
        {
            nh--;
            rp--;
        }
        else{
            nc--;
            rp--;
        }
    }
    private int vote_out()
    {
        int chosen_vote_out=(int)(Math.random()*this.N);
        while (!alive[chosen_vote_out])
        {
            chosen_vote_out=(int)(Math.random()*this.N);
        }
        return chosen_vote_out;
    }
    private int healer_target()
    {
        int chosen_healer_player=(int)(Math.random()*this.N);
        while (!alive[chosen_healer_player]||healer.contains(chosen_healer_player))
        {
            chosen_healer_player=(int)(Math.random()*this.N);
        }
        return chosen_healer_player;
    }
    private int detective_target()
    {
        int chosen_player=(int)(Math.random()*this.N);
        while (!alive[chosen_player]||detective.contains(chosen_player))
        {
            chosen_player=(int)(Math.random()*this.N);
        }
        return chosen_player;
    }
    private int mafia_target()
    {
        int chosen_mafia_player=(int)(Math.random()*this.N);
        while (!alive[chosen_mafia_player]||mafia.contains(chosen_mafia_player))
        {
            chosen_mafia_player=(int)(Math.random()*this.N);
        }
        return chosen_mafia_player;
    }

}
