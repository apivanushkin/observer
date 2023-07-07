package com.epam.rd.autocode.observer.git;

import com.epam.rd.autocode.observer.git.Event.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositoryImpl implements Repository {

	private final List<WebHook> webHooks;
	private final Map<String, List<Commit>> commits;

	public RepositoryImpl() {
		this.webHooks = new ArrayList<>();
		this.commits = new HashMap<>();
	}

	@Override
	public void addWebHook(WebHook webHook) {
		this.webHooks.add(webHook);
	}

	@Override
	public Commit commit(String branch, String author, String[] changes) {
		Commit commit = new Commit(author, changes);
		List<Commit> targetBranchCommits = Optional.ofNullable(commits.get(branch))
			.map(list -> Stream.concat(list.stream(), Stream.of(commit)).collect(Collectors.toList()))
			.orElse(List.of(commit));

		commits.put(branch, targetBranchCommits);

		dispatch(new Event(Type.COMMIT, branch, List.of(commit)));

		return commit;
	}

	@Override
	public void merge(String sourceBranch, String targetBranch) {
		List<Commit> targetBranchCommits = Optional.ofNullable(commits.get(targetBranch))
			.map(ArrayList::new)
			.orElse(new ArrayList<>());
		List<Commit> sourceBranchCommits = commits.get(sourceBranch);

		var difference = sourceBranchCommits.stream()
			.filter(c -> !targetBranchCommits.contains(c))
			.collect(Collectors.toList());

		if (!difference.isEmpty()) {
			targetBranchCommits.addAll(difference);
			commits.put(targetBranch, targetBranchCommits);

			dispatch(new Event(Type.MERGE, targetBranch, new ArrayList<>(difference)));
		}
	}

	private void dispatch(Event event) {
		this.webHooks.forEach(webHook -> webHook.onEvent(event));
	}
}
