{{/*
Expand the name of the chart.
*/}}
{{- define "demo-chart.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}


{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "demo-chart.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "demo-chart.labels" -}}
helm.sh/chart: {{ include "demo-chart.chart" . }}
{{ include "demo-chart.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "demo-chart.selectorLabels" -}}
app.kubernetes.io/name: {{ include "demo-chart.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}