package com.bograntex.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.bograntex.relatorio.SigiRelatorio;
import com.bograntex.utils.DateUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class ProgressBarRelatorio extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public final static int ONE_SECOND = 1000;
    private JProgressBar progressBar;
    private Timer timer;
    private JButton startButton;
    private LongTask task;
    private JTextArea taskOutput;
    private String newline = "\n";
    private String oldMessage;
    
    private SigiRelatorio relatorio;
    private Map<String, Object> paramsRelatorio;
    
    public ProgressBarRelatorio() {
        super(new BorderLayout());
        task = new LongTask();
        startButton = new JButton("Gerar Sigi");
        startButton.setActionCommand("gerar");
        startButton.addActionListener(this);
        
        UIManager.put("ProgressBar.background", Color.WHITE);
        UIManager.put("ProgressBar.foreground", new Color(50,205,50));
        UIManager.put("ProgressBar.selectionBackground", Color.RED);
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(Color.GRAY, 1, false));
        
        progressBar = new JProgressBar(0, task.getLengthOfTask());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        taskOutput = new JTextArea(10, 25);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
        taskOutput.setCursor(null);
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.add(startButton);
        panel.add(progressBar);
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //Create a timer.
        timer = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if(progressBar.getValue() == 0) {
            		paramsRelatorio = relatorio.getReportParameter();
            		taskOutput.setText("Iniciando relatório - "+ new SimpleDateFormat("HH:mm").format(new Date()) + newline);
            	} else {
            		paramsRelatorio = relatorio.getCalcRelatorioEstoque(paramsRelatorio);
            	}
                progressBar.setValue(relatorio.getCurrent());
                task.setCurrent(relatorio.getCurrent());
                String message = relatorio.getMessage();
                if (message != null && !(message.equals(oldMessage))) {
                	oldMessage = message;
                    taskOutput.append(message + newline);
                    taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
                }
                if (task.getCurrent() == 100) {
                    Toolkit.getDefaultToolkit().beep();
                    timer.stop();
                    startButton.setEnabled(true);
                    setCursor(null);
                    progressBar.setValue(0);
                    taskOutput.append("Relátorio Concluído - "+ new SimpleDateFormat("HH:mm").format(new Date()) + newline);
                    JasperPrint impressao = relatorio.createReport(paramsRelatorio);
                    JasperPrint reuniao = relatorio.createReportReuniao(paramsRelatorio);
                    JasperPrint setor = relatorio.createReportSetor(paramsRelatorio);
                    JasperPrint setor2 = relatorio.createReportSetor2(paramsRelatorio);
        			JasperViewer.viewReport(impressao, false);
        			try {
        				String path = this.getClass().getClassLoader().getResource("").getPath();
        				if (!path.contains("bin/")) {
        					JasperExportManager.exportReportToPdfFile(impressao, this.getClass().getClassLoader().getResource("").getPath()+"downloads/Sigi_"+DateUtils.dateToArquivo(new Date())+".pdf");
        					JasperExportManager.exportReportToPdfFile(reuniao, this.getClass().getClassLoader().getResource("").getPath()+"downloads/reuniao/Sigi_Reuniao_"+DateUtils.dateToArquivo(new Date())+".pdf");
        					JasperExportManager.exportReportToPdfFile(setor, this.getClass().getClassLoader().getResource("").getPath()+"downloads/setor/Sigi_Setor_"+DateUtils.dateToArquivo(new Date())+".pdf");
        					JasperExportManager.exportReportToPdfFile(setor2, this.getClass().getClassLoader().getResource("").getPath()+"downloads/setor/Sigi_Setor2_"+DateUtils.dateToArquivo(new Date())+".pdf");
        				}
        			} catch (JRException e) {
						e.printStackTrace();
					}
                }
            }
        });
    }
    /**
     * Called when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        timer.start();
        relatorio = new SigiRelatorio();
    }
    
}
class LongTask {
    private int lengthOfTask;
    private int current = 0;
    private boolean done = false;
    private String statMessage;
    
    public LongTask() {
        lengthOfTask = 100;
    }
    public int getLengthOfTask() {
        return lengthOfTask;
    }
    public int getCurrent() {
        return current;
    }
    public void setCurrent(int current) {
        this.current = current;
    }
    public void stop() {
        statMessage = null;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public String getMessage() {
        return statMessage;
    }
}

abstract class SwingWorker {
    private Object value;  // see getValue(), setValue()
    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }
    private ThreadVar threadVar;
    /** 
     * Get the value produced by the worker thread, or null if it 
     * hasn't been constructed yet.
     */
    protected synchronized Object getValue() { 
        return value; 
    }
    /** 
     * Set the value produced by worker thread 
     */
    private synchronized void setValue(Object x) { 
        value = x; 
    }
    /** 
     * Compute the value to be returned by the <code>get</code> method. 
     */
    public abstract Object construct();
    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {}
    
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }
    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {  
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }
    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };
        Runnable doConstruct = new Runnable() { 
            public void run() {
                try {
                    setValue(construct());
                }
                finally {
                    threadVar.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }
    /**
     * Start the worker thread.
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
    
    @SuppressWarnings("deprecation")
	public void stop() {
        Thread t = threadVar.get();
        if (t != null) {
            t.stop();
        }
    }

}
