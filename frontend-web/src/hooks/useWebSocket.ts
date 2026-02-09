import { useEffect } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { WS_BASE_URL } from "../config/api";

export const useWebSocket = (
  topic: string,
  onMessageReceived: (msg: any) => void
) => {
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(WS_BASE_URL),
      debug: () => {},
      onConnect: () => {
        client.subscribe(topic, (message) => {
          if (message.body) {
            onMessageReceived(JSON.parse(message.body));
          }
        });
      },
      onStompError: (frame) => {
        console.error("WS STOMP Error", frame);
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, [topic, onMessageReceived]);
};
