package com.epam.rd.autocode.observer.git;

import com.epam.rd.autocode.observer.git.Event.Type;

public class GitRepoObservers {
    public static Repository newRepository(){
        return new RepositoryImpl();
    }

    public static WebHook mergeToBranchWebHook(String branchName){
        return new WebHookImpl(branchName, Type.MERGE);
    }

    public static WebHook commitToBranchWebHook(String branchName){
        return new WebHookImpl(branchName, Type.COMMIT);
    }


}
