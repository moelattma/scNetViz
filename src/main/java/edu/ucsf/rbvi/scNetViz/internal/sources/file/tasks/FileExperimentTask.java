package edu.ucsf.rbvi.scNetViz.internal.sources.file.tasks;

import java.io.File;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import edu.ucsf.rbvi.scNetViz.internal.api.Experiment;
import edu.ucsf.rbvi.scNetViz.internal.api.Metadata;
import edu.ucsf.rbvi.scNetViz.internal.model.ScNVManager;
import edu.ucsf.rbvi.scNetViz.internal.sources.file.FileExperiment;
import edu.ucsf.rbvi.scNetViz.internal.sources.file.FileMetadata;
import edu.ucsf.rbvi.scNetViz.internal.sources.file.FileSource;
import edu.ucsf.rbvi.scNetViz.internal.tasks.ShowExperimentTableTask;

public class FileExperimentTask extends AbstractTask {
	final ScNVManager scManager;
	final FileSource fileSource;

	@Tunable (description="Species", required=true, 
	          tooltip="Species information is required for network generation")
	public String species = "Homo sapiens";

	@Tunable (description="Zip file with MTX matrix and headers",params="input=true")
	public File file;

	@Tunable (description="Show experiment table after loading",context="nogui")
	public boolean showTable = true;

	public FileExperimentTask(final ScNVManager scManager, final FileSource fileSource) {
		super();
		this.scManager = scManager;
		this.fileSource = fileSource;
	}

	@Override
	public void run(TaskMonitor taskMonitor) {
		taskMonitor.setTitle(getTitle());
		taskMonitor.setStatusMessage("Reading mtx file");
		FileMetadata metadata = new FileMetadata(file);
		metadata.put(Metadata.SPECIES, species);
		Experiment experiment = fileSource.getExperiment(metadata, taskMonitor);

		if (showTable) {
			// Show the experiment
			insertTasksAfterCurrentTask(new ShowExperimentTableTask(scManager, experiment));
		}
	}

	@ProvidesTitle
	public String getTitle() {return "Read mtx experiment file";}
}
