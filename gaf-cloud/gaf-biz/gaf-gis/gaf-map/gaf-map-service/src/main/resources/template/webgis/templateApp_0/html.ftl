<div>
    <gaf-map-basic-element v-if="loaded"/>
    <gaf-map-viewer
            :smSceneList="smSceneList"
            :smLayerList="smLayerList"
            :tiandituLayerList="tiandituLayerList"
            @initialize="onViewerLoaded"
                ></gaf-map-viewer>
    <div v-if="mapApp">

        <gaf-map-tool-bar-horizontal v-for="horizontalToolbar in mapApp.horizontalToolbars" :content="horizontalToolbar.buttons" :position="horizontalToolbar.position"/>
        <gaf-map-tool-bar-vertical v-for="verticalToolbar in mapApp.verticalToolbars" :content="verticalToolbar.buttons" :position="verticalToolbar.position"/>
        <gaf-map-draggable
                visible="true"
                title="资源目录"
                :width="280"
        >
            <gaf-map-tree
                    :replaceFields="mapApp.resourceTree.replaceFields"
                    :data-list="mapApp.resourceTree.allDataList"
                    :searchInputShow="true"
                    :check="onTreeNodeChecked"
                    :select="onSelect"
                    :allCheckable="false"
                    :leafnodeCheckable="true"
                    :someNodeCheckable="false"
            />
        </gaf-map-draggable>
    </div>
</div>