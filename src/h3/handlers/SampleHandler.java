package h3.handlers;

import org.eclipse.jdt.core.dom.Modifier;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			extractMethodRefactoring();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void extractMethodRefactoring() throws CoreException {
		int start = 20;
		int length = 1;

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject m_project = root.getProject("hello");

		m_project.open(null);
		IJavaProject javaProject = JavaCore.create(m_project);
		IProjectDescription description = m_project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		m_project.setDescription(description, null);

		IFolder folder = m_project.getFolder("src");

		IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);

		IPackageFragment fragment = srcFolder.getPackageFragment("");

		NullProgressMonitor pm = new NullProgressMonitor();
		fragment.open(pm);

		ICompilationUnit[] cus = fragment.getCompilationUnits();
		ICompilationUnit cu = cus[0];

		ExtractMethodRefactoring refactoring = new ExtractMethodRefactoring(cu, start, length);
		refactoring.setMethodName("extracted");
		refactoring.setVisibility(Modifier.PUBLIC);
		final PerformRefactoringOperation op = new PerformRefactoringOperation(refactoring,
				CheckConditionsOperation.ALL_CONDITIONS);
		JavaCore.run(op, new NullProgressMonitor());
	}
}
