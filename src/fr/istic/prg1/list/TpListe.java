package fr.istic.prg1.list;

import java.awt.Font;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2022-09-23
 */

public class TpListe {

	private static final int MAX_SET = 5;
	private static final Scanner standardInput = new Scanner(System.in);
	private static final String SET_N = "l'ensemble numero ";

	private static ArrayList<MySet> setList = new ArrayList<>(MAX_SET);

	private static final String[] COMMAND_TEXTS = { "afficher les entiers appartenant a l'ensemble numero n1",
			"afficher les rangs presents dans l'ensemble numero n1", "ajouter des valeurs a l'ensemble numero n1",
			"determiner si x appartient a l'ensemble numero n1", "afficher le cardinal de l'ensemble numero n1",
			"l'ensemble numero n1 <-- difference l'ensemble numero n1 \\ l'ensemble numero n2)",
			"l'ensemble numero n1 <-- difference symetrique de l'ensemble numero n1 et l'ensemble numero n2",
			"determiner si l'ensemble numero n1 et l'ensemble numero n2 sont egaux",
			"determiner si l'ensemble numero n1 est inclus dans l'ensemble numero n2",
			"l'ensemble numero n1 <-- intersection de l'ensemble numero n1 et l'ensemble numero n2",
			"reinitialiser l'ensemble numero n1 a partir d'un fichier", "retirer des valeurs a l'ensemble numero n1",
			"sauvegarder l'ensemble numero n1 dans un fichier",
			"l'ensemble numero n1 <-- union de l'ensemble numero n1 et l'ensemble numero n2", "arret de l'execution" };

	private static final String[] ITEM_NAMES = { "Afficher l'ensemble", "Afficher les rangs", "Ajouter", "Appartient",
			"Cardinal", "Difference", "Difference symetrique", "Egaux", "Inclus", "Intersection", "Reinitialiser",
			"Retirer", "Sauvegarder", "Union", "Quitter" };

	private static Frame menu = new Frame();
	private static final int H = 200;
	private static final int V = 480;
	private static final int LOCX = 800;
	private static final int LOCY = 10;
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 30;

	private static class Action implements ActionListener {
		private int actionNumber;

		public Action(int actionNumber) {
			this.actionNumber = actionNumber;
		}

		@Override
		public synchronized void actionPerformed(ActionEvent event) {
			menu.setVisible(false);
			execute(actionNumber);
			menu.setVisible(true);
		}

		/**
		 * 
		 * Faire le traitement associé à l'item de menu sélectionné.
		 * 
		 * @param number choix du menu sélectionné
		 */
		private static void execute(int number) {
			System.out.println("* " + COMMAND_TEXTS[number]);
			int n1;
			int n2;
			boolean b;
			switch (number) {
			case 0:
				setList.get(readSetNumber(1)).print();
				break;
			case 1:
				setList.get(readSetNumber(1)).printRanks();
				break;
			case 2:
				setList.get(readSetNumber(1)).add();
				break;
			case 3:
				b = setList.get(readSetNumber(1)).contains();
				if (b) {
					System.out.println(" valeur presente");
				} else {
					System.out.println(" valeur non presente");
				}
				break;
			case 4:
				n1 = readSetNumber(1);
				int size = setList.get(n1).size();
				System.out.println(SET_N + n1 + " a " + size + " elements");
				break;
			case 5:
				setList.get(readSetNumber(1)).difference(setList.get(readSetNumber(2)));
				break;
			case 6:
				setList.get(readSetNumber(1)).symmetricDifference(setList.get(readSetNumber(2)));
				break;
			case 7:
				n1 = readSetNumber(1);
				n2 = readSetNumber(2);
				b = setList.get(n1).equals(setList.get(n2));
				if (b) {
					System.out.println("les ensembles numero " + n1 + "et numero " + n2 + " sont egaux");
				} else {
					System.out.println("les ensembles numero " + n1 + " et numero " + n2 + " ne sont pas egaux");
				}
				break;
			case 8:
				n1 = readSetNumber(1);
				n2 = readSetNumber(2);
				b = setList.get(n1).isIncludedIn(setList.get(n2));
				if (b) {
					System.out.println(SET_N + n1 + "est inclus dans l'ensemble numero " + n2);
				} else {
					System.out.println(SET_N + n1 + "n'est pas inclus dans l'ensemble numero " + n2);
				}
				break;
			case 9:
				setList.get(readSetNumber(1)).intersection(setList.get(readSetNumber(2)));
				break;
			case 10:
				setList.get(readSetNumber(1)).restore();
				break;
			case 11:
				setList.get(readSetNumber(1)).remove();
				break;
			case 12:
				setList.get(readSetNumber(1)).save();
				break;
			case 13:
				setList.get(readSetNumber(1)).union(setList.get(readSetNumber(2)));
				break;
			case 14:
				for (int j = 0; j < MAX_SET; ++j) {
					setList.set(j, null);
				}
				standardInput.close();
				MySet.closeAll();
				System.exit(0);
				break;
			default:
			}
		}

		/**
		 * @param i
		 * @return numéro de l'ensemble choisi par l'utilisateur
		 */
		private static int readSetNumber(int i) {
			int number;
			boolean b;
			do {
				b = true;
				number = 0;
				System.out.println("  numero d'ensemble n" + i + " (>=0 et <" + MAX_SET + ") : ");
				try {
					number = standardInput.nextInt();
				} catch (NumberFormatException e) {
					b = false;
				}
			} while (!b || (number < 0 || number >= MAX_SET));
			return number;
		}
	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < MAX_SET; ++i) {
				setList.add(new MySet());
			}
			menu.setTitle("TP listes");
			menu.setSize(H, V);
			menu.setLocation(LOCX, LOCY);
			menu.setLayout(null);
			for (int i = 0; i < ITEM_NAMES.length; ++i) {
				Button button = new Button(ITEM_NAMES[i]);
				button.addActionListener(new Action(i));
				button.setLocation(0, 25 + BUTTON_HEIGHT * i);
				button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
				button.setFont(new Font("serif", Font.BOLD, 14));
				menu.add(button);
			}
			menu.setVisible(true);
		} catch (AssertionError e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

}