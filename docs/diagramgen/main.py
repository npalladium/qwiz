from diagrams import Diagram, Cluster, Edge
from diagrams.onprem.database import Mongodb
from diagrams.programming.language import Java
from diagrams.programming.framework import Angular
from diagrams.programming.framework import Spring
from diagrams.onprem.container import Docker
from diagrams.saas.chat import Discord
from diagrams.generic.device import Tablet

with Diagram():
    discord = Discord()

    with Cluster("Containerized"):
        container = Docker()

        with Cluster("Backend"):
            mongo = Mongodb("Database")
            app = Spring("API")
            app >> Edge(color="brown") >> mongo

        frontend = Angular("Web App")
        frontend >> Edge(color="black") >> app

    user = Tablet("User")
    user >> Edge(color="black") >> frontend
    user << Edge(color="black") >> discord
    app << Edge(color="black") >> discord

