{{- define "mychart.labels" }}
  labels:
    date: {{ now | htmlDate }}
    version: "0.1"
{{- end }}
