/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.develop.entity.PresignUploadRequest;
import com.supermap.desktop.develop.exception.FileDownloadException;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.ui.DialogLoginGaf;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;
import com.supermap.desktop.develop.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.security.sasl.AuthenticationException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class GafClient {
    private static final String token_endpoint = "%s/authentication/token";
    private static final String refresh_token_endpoint = "%s/authentication/token/refresh";
    private static final String workspace_endpoint = "%s/data-mgt/data-workspaces";
    private static final String datasource_endpoint = "%s/data-mgt/sys-resource-datasources";
    private static final String file_upload_endpoint = "%s/storage/api/tenant-created-first/default/upload%s";
    private static final String file_download_endpoint = "%s/storage/api/tenant-created-first/default/download%s";
    private static final String file_metadata_endpoint = "%s/storage/api/tenant-created-first/default/metadata%s";
    private String username;
    private String password;
    private String host;
    private String token;
    private String refreshToken;
    private static GafClient INSTANCE;

    public static GafClient instance() {
        if (INSTANCE == null) {
//            JOptionPaneUtilities.showErrorMessageDialog("请登录");
            new DialogLoginGaf().showDialog();
        }
        return INSTANCE;
    }

    public static void logout() {
        if (INSTANCE != null) {
            INSTANCE = null;
        }
    }

    public GafClient(String username, String password, String host) {
        this.username = username;
        this.password = password;
        this.host = host;
        try {
            JSONObject re = getToken(username, password, host);
            this.token = re.getString("access_token");
            this.refreshToken = re.getString("refresh_token");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("登录失败：" + e.getMessage());
        }
        INSTANCE = this;
    }

    public JSONObject getToken(String username, String password, String host) throws Exception {
        String url = String.format(token_endpoint, host);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("username", username);
        urlParams.put("password", password);
        JSONObject re = (JSONObject) HttpUtils.doPostJson(url, null, urlParams, JSONObject.class);
        checkResult(re);
        JSONObject data = re.getJSONObject("data");
        if (data.getLongValue("expires_in") < 3600) {
            return refreshToken(data.getString("refresh_token"), host);
        }
        return data;
    }

    public JSONObject refreshToken(String refreshToken, String host) throws Exception {
        String url = String.format(refresh_token_endpoint, host);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair pair = new BasicNameValuePair("refresh_token", refreshToken);
        params.add(pair);
        JSONObject re = (JSONObject) HttpUtils.doPostJson(url, params, JSONObject.class);
        checkResult(re);
        ApplicationContextUtils.getOutput().output("刷新token");
        return re.getJSONObject("data");
    }

    public JSONArray workspaceList() throws Exception {
        String url = String.format(workspace_endpoint, host);
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("pageNum", "0");
        urlParams.put("pageSize", "0");
        JSONObject re = (JSONObject) apiWork(token -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            return HttpUtils.doGet(url, urlParams, headers, JSONObject.class);
        });
//        JSONObject re = (JSONObject) HttpUtils.doGet(url,urlParams,headers,JSONObject.class);
        checkResult(re);
        return re.getJSONObject("data").getJSONArray("pageList");
    }

    public JSONArray datasourceTree() throws Exception {
        String url = String.format(datasource_endpoint + "/tree", host);
        JSONObject re = (JSONObject) apiWork(token -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            return HttpUtils.doGet(url, null, headers, JSONObject.class);
        });
        checkResult(re);
        return re.getJSONArray("data");
    }

    public void registryWorkspace(WorkspaceConnectionInfo connectionInfo, String path) throws Exception {
        String server = connectionInfo.getServer();
        if (CommonUtils.isFileTypeSource(connectionInfo)) {
            server = path + Paths.get(server).getFileName().toString();
        }
        String url = String.format(workspace_endpoint, host);
        Map<String, String> body = new HashMap<>();
        body.put("wsName", connectionInfo.getName());
        body.put("typeCode", connectionInfo.getType().name());
        body.put("server", server);
        body.put("database", connectionInfo.getDatabase());
        body.put("userName", connectionInfo.getUser());
        body.put("password", connectionInfo.getPassword());
        JSONObject re = (JSONObject) apiWork(token -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            return HttpUtils.doPostJson(url, JSON.toJSONString(body), null, headers, JSONObject.class);
        });
        checkResult(re);
    }

    public void registryDatasource(DatasourceConnectionInfo connectionInfo, String catalogCode, String path) throws Exception {
        String server = connectionInfo.getServer();
        if (CommonUtils.isFileTypeSource(connectionInfo)) {
            server = path + Paths.get(server).getFileName().toString();
        }
        String url = String.format(datasource_endpoint, host);
        Map<String, Object> body = new HashMap<>();
        body.put("addr", server);
        body.put("typeCode", connectionInfo.getEngineType().name());
        body.put("dbName", connectionInfo.getDatabase());
        body.put("dsName", connectionInfo.getAlias());
        body.put("userName", connectionInfo.getUser());
        body.put("password", connectionInfo.getPassword());
        body.put("catalogCode", catalogCode);
        body.put("isSdx", true);
        JSONObject re = (JSONObject) apiWork(token -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            return HttpUtils.doPostJson(url, JSON.toJSONString(body), null, headers, JSONObject.class);
        });
        checkResult(re);
    }

    public PresignUploadRequest uploadPresignUrl(String keyName, String contentMd5) throws FileUploadException {
        try {
            String url = String.format(file_upload_endpoint, host, keyName);
            JSONObject re = (JSONObject) apiWork(token -> {
                Map<String, String> headers = new HashMap<>();
                if (!StringUtils.isEmpty(contentMd5)) {
                    headers.put("contentMd5", contentMd5);
                }
                headers.put("Authorization", "Bearer " + token);
                return HttpUtils.doPutJson(url, null, null, headers, JSONObject.class);
            });
            checkResult(re);
            return new PresignUploadRequest(re.getString("data"), contentMd5);
        } catch (Exception e) {
            throw new FileUploadException(Paths.get(keyName).getFileName().toString());
        }
    }

    public PresignUploadRequest uploadPresignUrl(String keyName) throws FileUploadException {
        return uploadPresignUrl(keyName, null);
    }

    public String downloadPresignUrl(String keyName) throws FileDownloadException {
        try {
            String url = String.format(file_download_endpoint, host, keyName);

            JSONObject re = (JSONObject) apiWork(token -> {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return HttpUtils.doGet(url, null, headers, JSONObject.class);
            });
            //        JSONObject re = (JSONObject) HttpUtils.doGet(url,null,headers,JSONObject.class);
            checkResult(re);
            return re.getString("data");
        } catch (Exception e) {
            throw new FileDownloadException(Paths.get(keyName).getFileName().toString());
        }
    }

    public JSONObject objectMetadata(String keyName) throws Exception {
        String url = String.format(file_metadata_endpoint, host, keyName);
        JSONObject re = (JSONObject) apiWork(token -> {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            return HttpUtils.doGet(url, null, headers, JSONObject.class);
        });
        checkResult(re);
        return re;

    }

    private Object apiWork(ApiWork work) throws Exception {
        Object re = null;
        try {
            re = work.run(this.token);
        } catch (AuthenticationException e) {
            if (StringUtils.isEmpty(this.refreshToken)) {
                JSONObject result = getToken(this.username, this.password, this.host);
                this.token = result.getString("access_token");
                this.refreshToken = result.getString("refresh_token");
            } else {
                JSONObject result = refreshToken(this.refreshToken, this.host);
                this.token = result.getString("access_token");
                this.refreshToken = result.getString("refresh_token");
            }
            re = work.run(this.token);
        }
        return re;
    }


    private void checkResult(JSONObject re) {
        if (!re.getBoolean("successed")) {
            ApplicationContextUtils.getOutput().output(JSON.toJSONString(re));
            throw new RuntimeException("操作失败：" + re.getString("message"));
        }
    }

    static interface ApiWork {
        Object run(String token) throws Exception;
    }
}
