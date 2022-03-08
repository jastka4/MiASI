package tb.antlr;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import tb.antlr.interpreter.TExpr1;
import tb.antlr.kompilator.TExpr3;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Okno extends JFrame {

	private JPanel contentPane;

	private JTextPane inputPane;
	private JScrollPane inputPaneScrollable;
	private JButton parseButton;
	
	private JTextPane astPane;
	private JScrollPane astPaneScrollable;
	private JButton interpretButton;

	private JTextPane resultPane;
	private JScrollPane resultPaneScrollable;
	private JButton compileButton;
	
	private JTextPane assemblyPane;
	private JScrollPane assemblyPaneScrollable;

	private CommonTreeNodeStream nodes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Okno frame = new Okno();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Okno() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		inputPane = new JTextPane();
		inputPane.setBounds(5, 5, 280, 280);
		inputPane.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
//				System.out.println("w remove");
				invalidTree(true);
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
//				System.out.println("w insert");
				invalidTree(true);
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
//				System.out.println("w changed");
				invalidTree(true);
				
			}
		});
		inputPaneScrollable = new JScrollPane(inputPane,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		inputPaneScrollable.setBounds(inputPane.getBounds());
		contentPane.add(inputPaneScrollable);

		parseButton = new JButton("Parse");
		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doParse();
			}
		});
		parseButton.setBounds(295, 5, 150, 23);
		contentPane.add(parseButton);

		astPane = new JTextPane();
		astPane.setEditable(false);
		astPane.setBounds(455, 5, 280, 280);
		astPaneScrollable = new JScrollPane(astPane,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		astPaneScrollable.setBounds(astPane.getBounds());
		
		contentPane.add(astPaneScrollable);

		interpretButton = new JButton("Interpretuj (TExpr1)");
		interpretButton.setEnabled(false);
		interpretButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTreeParseInterpret();
			}
		});
		interpretButton.setBounds(5, 348, 280, 23);
		contentPane.add(interpretButton);

		resultPane = new JTextPane();
		resultPane.setEditable(false);
		resultPane.setBounds(5, 382, 280, 280);
		resultPaneScrollable = new JScrollPane(resultPane,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		resultPaneScrollable.setBounds(resultPane.getBounds());
		
		contentPane.add(resultPaneScrollable);

		compileButton = new JButton("Kompiluj(TExpr3)");
		compileButton.setEnabled(false);
		compileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTreeParseCompile();
			}
		});
		compileButton.setBounds(455, 348, 280, 23);
		contentPane.add(compileButton);

		assemblyPane = new JTextPane();
		assemblyPane.setEditable(false);
		assemblyPane.setBounds(455, 382, 280, 280);
		assemblyPaneScrollable = new JScrollPane(assemblyPane,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		assemblyPaneScrollable.setBounds(assemblyPane.getBounds());
		
		contentPane.add(assemblyPaneScrollable);

	}

	protected void invalidTree(boolean b) {
		interpretButton.setEnabled(!b);
		compileButton.setEnabled(!b);
		astPane.setForeground(b ? Color.LIGHT_GRAY : Color.BLACK);
	}

	private void doParse() {
		// Tworzymy analizator leksykalny i każemy mu czytać z okna
		ANTLRStringStream input = new ANTLRStringStream(inputPane.getText());
		ExprLexer lexer = new ExprLexer(input);

		// Tworzymy bufor na tokeny pomiędzy analizatorem leksykalnym a parserem
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Tworzymy parser czytający z powyższego bufora
		ExprParser parser = new ExprParser(tokens);
// poniższa wersja dla debuggowania ANTLRWorksem używając niestandardowego portu 		
//		ExprParser parser = new ExprParser(tokens,49102,null);

		// Wywołujemy parser generujący drzewo startując od reguły prog (Z klasy Expr)
		ExprParser.prog_return root = null;
		try {
			root = parser.prog();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		// Wypisujemy drzewo w oknie ast
		astPane.setText(root.tree.toStringTree());

		// Tworzymy bufor na węzły drzewa
		nodes = new CommonTreeNodeStream(root.tree);
		nodes.setTokenStream(tokens);
		
		invalidTree(false);
	}

	private void doTreeParseInterpret() {
		// Tworzymy parser drzew korzystający z powyższego bufora
		TExpr1 walker = new TExpr1(nodes);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
		System.setOut(ps);

		// Wywołujemy parser drzew - startując od reguły prog (Tym razem z klasy
		// TExpr1!)
		try {
			walker.prog();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

		resultPane.setText(baos.toString());
		System.setOut(old);
		nodes.reset();
	}

	private void doTreeParseCompile() {
		String packagePath = TExpr3.class.getPackage().getName().replaceAll("\\.", "/");

		// Ładujemy szablony
		FileReader groupFile;
		StringTemplateGroup templates = null;
		try {
			groupFile = new FileReader("src/" + packagePath + "/pierwszy.stg");
			templates = new StringTemplateGroup(groupFile);
			groupFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// Tworzymy parser drzew korzystający z powyższego bufora
		TExpr3 walker = new TExpr3(nodes);
// poniższa wersja dla debuggowania ANTLRWorksem używając niestandardowego portu 		
//		TExpr3 walker = new TExpr3(nodes,49103,null);

		// Tłumaczymy parserowi drzew, jakich szablonów ma używać
		walker.setTemplateLib(templates);

		// Wywołujemy parser drzew - startując od reguły prog (Tym razem z klasy
		// TExpr3!)
		TExpr3.prog_return tpl = null;
		try {
			tpl = walker.prog();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Pobierz "wypełniony" szablon
		StringTemplate stp = (StringTemplate) tpl.getTemplate();

		// Wypisujemy wypełniony szablon
		assemblyPane.setText(stp.toString());

		nodes.reset();
	}

}
